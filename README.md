# GearMax-API

This is a Spring Boot backend project that provides REST APIs to deliver web services.

### Technologies
The technologies that the project uses:
- Spring Boot
- Spring JPA
- MySQL

### Environment Requirements
To run this project locally, your environments must meet the following criteria:
- Installed JDK (at least 11) and Maven
- MySQL Server 8.0+

### How to run this project locally for development purpose?
1. Go to *gearmax-api* and run `mvn clean install`.
2. Set up a database named *demo* with MySQL.
3. Find the file *gearmax-api/src/main/resources/application.properties* and change the configuration:
    - spring.datasource.username=[your MySQL username]
    - spring.datasource.password=[your MySQL password]
4. Run *main()* function in *GearmaxApiApplication* class.