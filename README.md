# GearMax-API

GearMax is a web application that helps buyers find the best-fit cars, assists sellers to post their used cars and provides 
price prediction functionalities.

For a lot of reasons such as improving the scalability, we separate the web app into three services: backend, frontend and database. 
The code of the three services is managed in three git repositories. This project, **GearMax-API** is responsible for providing the 
backend service. Specifically, this is a Spring Boot backend project that provides REST APIs to deliver web services.

### The 12-Factor Methodology

GearMax follows the 12-factor methodology, which is the best practice for SaaS development, to develop applications that 
run as services.

For more explanations:

[What is the Twelve-Factor App?](https://12factor.net/)

[Twelve-Factor Methodology in a Spring Boot Microservice](https://www.baeldung.com/spring-boot-12-factor)

### Technologies
The technologies that the project uses:
- Spring Boot
- Spring JPA
- MySQL
- React (gearmax-react-app)

### Environment Requirements
To run this project locally, your environments must meet the following criteria:
- Installed JDK (at least 11) and Maven
- MySQL Server 8.0+

### Project Structure

Use car-related classes as the example:

        com.uw.gearmax.gearmaxapi
         +- GearmaxApiApplication.java
         |
         +- controller
         |   +- viewobject
         |   |    +- CarVO.java
         |   |
         |   +- CarController.java
         |   
         +- domain
         |   +- Car.java
         |
         +- repository
         |   +- CarRepository.java
         |
         +- service
         |   +- impl
         |   |    +- CarServiceImpl.java
         |   +- CarService.java
         |
         +- response
         |
         +- error
         |
         +- validator

The *controller* package includes classes annotated as *@Controller*, which handles HTTP requests. Within each request 
function, you can invoke *service* functions but do not directly invoke *repository* functions.

The *service* package includes interfaces that defines basic behaviors of web services. The *service/impl/* package 
includes classes annotated as *@Service*, which implements the corresponding interfaces and defines the concrete functions.
For example, interface *CarService* specifies the behavior *saveCar()*, then there must be a class titled *CarServiceImpl* 
to implement this function. In each service function, you can invoke *repository* functions to deal with real data.

The *repository* package includes interfaces that extends *CrudRepository* or *JpaRepository*. These interfaces inherently 
implements SQL functions to add/delete/update data, and what we have to do is just calling those APIs such as `carRepository.save(car)` 
in service level.

The *domain* package includes classes annotated as *@Entity*, which are POJOs representing data that can be persisted to
the database. An entity represents a table; for instance, the entity class *Car* represents a table called *Car*.
If you create a new *Car* object and store the object via `carRepository.save(car)`, then a record will be stored in the *Car* table.

The *controller/viewobject* includes classes which represents view objects. Those classes are very similar to entity classes, 
but they are wrapped to only have attributes expected to be displayed to users/developers. In controllers, we only return view 
objects rather than entity objects.

### How to run this app in either dev or prod environment?

If you would like to run this app in dev (development) environment, go to *gearmax-api/src/main/resources/application.properties* 
and specify `spring.profiles.active=dev`. This specification will deploy the configurations in *application-dev.properties* when you start your app.

If you would like to run this app in prod (production) environment, find *application.properties* and specify
`spring.profiles.active=prod`. This specification will deploy the configurations in *application-prod.properties* when you start your app.

Then go to *gearmax-api* and run `mvn clean install`.

#### How to configure this app in dev environment, which requires using your own MySQL server?

1. Set up a database named *demo* with MySQL.
2. Find the file *gearmax-api/src/main/resources/application-dev.properties* and change the configuration:
    - spring.datasource.username=[your MySQL username]
    - spring.datasource.password=[your MySQL password]
3. Run *main()* function in *GearmaxApiApplication* class.

#### How to configure this app in prod environment, which requires using the production MySQL server?

If you would like to change the production environment settings/configurations, find *gearmax-api/src/main/resources/application-prod.properties* 
and update the settings.

To avoid leaking sensitive information, we use [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot) to do the password encryption. 
Jasypt allows you to only specify the encrypted password in the *application.properties* file. For example, if you would like to 
encrypt a value *'InfoToBeEncrypted'* using the salt *'test'*, you can run command `mvn jasypt:decrypt-value -Djasypt.encryptor.password="test" -Djasypt.plugin.value="InfoToBeEncrypted"`.
You will see the output encrypted value *ENC(/nJHoNctIHpmuaGcmqqUIt5EPw/3/CWzz7RVZrOdhof3NnyvMezwO84n+WdESXLu)*, so you can 
replace the old original password with the encrypted value. Also, do not forget to specify your salt in the property file as well: `jasypt.encryptor.password=test`.

### How to send requests after the app starts?

You can directly send http requests or use Postman. Either way is fine.

**Try searching a car by given parameters**:

`http://localhost:8080/car/list?bodyType=SUV_Crossover&year=2010-2019`

`http://localhost:8080/car/list?bodyType=SUV_Crossover&year=2010-2019&mileage=50000`


**Only add/delete record to your local MySQL databases. Don't perform these operations to the production server.**

**Try adding a car record**:

- use Postman:
  ![Add Car by Postman](/img/postman_add_car.png)
  
Then you will see the new record in database.
      
**Try deleting a car record**:

- use Postman:
![Delete Car by Postman](img/postman_delete_car.png)
  
### For Contributors 

If you would like to contribute to this project, make a new git branch and work on that branch. Make sure to commit the 
code to the new branch and make a pull request to merge to the main branch.

Before committing the code, always reformat the code. In Intellij IDEA, the formatting shortcut for Windows is **Ctrl+Alt+Shift+L**.

Also, we recommend using SonarLint to fix your code quality issues before your committing the code. Here is the [link](https://www.sonarlint.org/) 
of SonarLint IDE extensions.
