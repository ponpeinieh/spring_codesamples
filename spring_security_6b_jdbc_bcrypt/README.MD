This is Spring demo from Udemy Spring & Hibernate course.   
Spring Security with JDBC
- Use Password encryption (BCrypt)
    - Spring team suggests using BCrypt algorithm
    - Perform one-way encrypted hashing 
        - The password from db is NEVER decrypted, because it's a one-way algorithm
    - Add a random salt to the password for additional protection
    - Include support to defeat brute force attacks
- How to get a bcrypt password
    - Option 1: Use a website utility to perform the encryption
        - Go to `https://www.bcryptcalculator.com/`
        - Enter the plain password and get the password hash.
            - Multiple runs will generate different result due to random salting
        - The database schema need to be changed, since **the total length of password should be 68**.
            - {bcrypt} - 8 chars
            - encoded password - 60 chars

            ```
            CREATE TABLE `users` (
            `username` varchar(50) NOT NULL,
            `password` varchar(68) NOT NULL,
            `enabled` tinyint(1) NOT NULL,
            PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
            ```
    - Option 2: Write Java code to perform the encryption