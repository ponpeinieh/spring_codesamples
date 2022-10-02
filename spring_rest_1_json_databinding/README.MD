This is Spring demo from Udemy Spring & Hibernate course.   
REST 
- JSON Basic
    ```
    {
    "id": 14,
    "firstName": "Mario",
    "lastName": "Rossi",
    "active": true
    }
    ```
    - **Curley braces define objects in JSON**
    - Object members are name / value pairs
    - Delimited by colons
    - **Name is always in double-quotes**
    - JSON Values
        - Numbers: no quotes
        - String: in double quotes
        - Boolean: true, false
        - **Nested JSON object**
        - Array : use square brackets
        - null
- Java JSON Data Binding: the process of converting JSON data to a Java POJO
    - Jackson API
        - Spring uses the Jackson Project behind the scenes
        - Jackson handles data binding between JSON and Java POJO
        - Jackson Data Binding API
            - Package: `com.fasterxml.jackson.databind`
            - Maven Dependency

            ```
            <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.4</version>
            </dependency>
            ```

        - By default, during data binding, Jackson will call getter/setter methods
            - It does NOT access internal private fields directly
            - From JSON to POJO - call setter methods
                - use an ObjectMapper 

                ```
                import java.io.File;
                import com.fasterxml.jackson.databind.ObjectMapper;
                public class Driver {
                    public static void main(String[] args) throws Exception {
                    // create object mapper
                    ObjectMapper mapper = new ObjectMapper();
                    // read JSON from file and map/convert to Java POJO
                    Student myStudent = mapper.readValue(new File("data/sample.json"), Student.class);
                    // also print individual items
                    System.out.println("First name = " + myStudent.getFirstName());
                    System.out.println("Last name = " + myStudent.getLastName());
                    }
                }
                ```

            - From POJO to JSON - call getter methods 

                ```
                // create object mapper
                ObjectMapper mapper = new ObjectMapper();
                // read JSON from file and map/convert to Java POJO
                Student myStudent = mapper.readValue(new File("data/sample.json"), Student.class);
                ...
                // now write JSON to output file
                mapper.enable(SerializationFeature.INDENT_OUTPUT); //make it pretty(indented)
                mapper.writeValue(new File("data/output.json"), myStudent);
                ```

- Spring and Jackson Support
    - When building Spring REST applications
        - **Spring will automatically handle Jackson Integration**
        - JSON data being passed to `REST controller` is converted to POJO
        - Java object being returned from `REST controller` is converted to JSON

- If JSON contains some properties not recognized by Jackson, it throws `UnrecognizedPropertyException`

```
com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "company" (class com.example.jackson.json.demo.Student), not marked as ignorable (6 known properties: "lastName", "address", "active", "id", "firstName", "languages"])
 at [Source: (File); line: 14, column: 14] (through reference chain: com.example.jackson.json.demo.Student["company"])
	at com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.from(UnrecognizedPropertyException.java:61)
	at com.fasterxml.jackson.databind.DeserializationContext.handleUnknownProperty(DeserializationContext.java:1127)
	at com.fasterxml.jackson.databind.deser.std.StdDeserializer.handleUnknownProperty(StdDeserializer.java:2023)
	at com.fasterxml.jackson.databind.deser.BeanDeserializerBase.handleUnknownProperty(BeanDeserializerBase.java:1700)
	at com.fasterxml.jackson.databind.deser.BeanDeserializerBase.handleUnknownVanilla(BeanDeserializerBase.java:1678)
	at com.fasterxml.jackson.databind.deser.BeanDeserializer.vanillaDeserialize(BeanDeserializer.java:320)
	at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:177)
	at com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:323)
	at com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:4674)
	at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3494)
	at com.example.jackson.json.demo.Driver.main(Driver.java:18)

```
- You can annotate the class to `ignore unknown properties`

```
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

``