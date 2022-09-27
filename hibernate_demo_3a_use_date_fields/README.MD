This is Spring demo from Udemy Spring & Hibernate course. It includes hibernate demo.
- How to make hibernate create schema DDL(eg. create table, update table schema)?
    - use `hbm2ddl` Configuration
    - <property name="hbm2ddl.auto">create</property> enable table creation when SessionFactory Object is created.
    - <property name="hbm2ddl.auto">update</property> enable table schema update when SessionFactory Object is created.
- Change mysql dialect to MySQL8Dialect, so the hbm2ddl create an InnoDB table.
    - <property name="dialect">org.hibernate.dialect.MySQL8Dialect</property>

```
create table customer (id integer not null auto_increment, create_date datetime(6), date_of_birth date, email varchar(255), first_name varchar(255), last_name varchar(255), update_date datetime(6), primary key (id)) engine=InnoDB
```

- Add Date field
    - use @Temporal annotation with TemporalType
    - javax.persistence.Temporal
    - javax.persistence.TemporalType
    - TemporalType has DATE, TIME, TIMESTAMP three types

```
@Column(name = "date_of_birth")
@Temporal(TemporalType.DATE)
private Date dateOfBirth;

```
- Transform between String and java.util.Date object

``` 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date; 

public class DateUtils {
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	// read a date string and parse/convert to a date
	public static Date parseDate(String dateStr) throws ParseException {
		Date theDate = formatter.parse(dateStr);

		return theDate;
	}

	// read a date and format/convert to a string
	public static String formatDate(Date theDate) {

		String result = null;

		if (theDate != null) {
			result = formatter.format(theDate);
		}

		return result;
	}

}

```
- Timestamp - store the data creation time and update time
    - use `@PrePersist` on callback which set the creation time
    - use `@PreUpdate` on callback which set the update time
    - javax.persistence.PrePersist, javax.persistence.PreUpdate
    - also change the timezone in the connection url in hibernate.cfg.xml
        - <property name="connection.url">jdbc:mysql://localhost:3306/hb_student_tracker?useSSL=false&amp;serverTimezone=Asia%2FTaipei</property>
		- use timezone of `Asia/Taipei`, but use url encoding of `%2F` for the slash character
            - URL encoding replaces unsafe ASCII characters with **a "%" followed by two hexadecimal digits.**
        - or use `GMT+8`, but need to url encode + character, ie. `GMT%2B8`
        

```
@Column(name = "create_date")
@Temporal(TemporalType.TIMESTAMP)
private Date createDate;

@Column(name = "update_date")
@Temporal(TemporalType.TIMESTAMP)
private Date updateDate;

@PrePersist
protected void onCreate() {
    createDate = new Date();
}

@PreUpdate
protected void onUpdate() {
    updateDate = new Date();
}
```