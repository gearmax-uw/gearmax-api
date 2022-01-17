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

### How to run this app locally for development purpose?

1. Go to *gearmax-api* and run `mvn clean install`.
2. Set up a database named *demo* with MySQL.
3. Find the file *gearmax-api/src/main/resources/application.properties* and change the configuration:
    - spring.datasource.username=[your MySQL username]
    - spring.datasource.password=[your MySQL password]
4. Run *main()* function in *GearmaxApiApplication* class.

### How to send requests after the app starts?

You can directly send http requests or use Postman. Either way is fine.

Try searching a car by given parameters:

`http://localhost:8080/car/list?bodyType=SUV_Crossover&year=2010-2019`

`http://localhost:8080/car/list?bodyType=SUV_Crossover&year=2010-2019&mileage=50000`


**Only add/delete record to your local MySQL databases. Don't perform these operations to the production server.**

Try adding a car record:

- use Postman:
  ![Add Car by Postman](/img/postman_add_car.png)
  
Then you will see the new record in database.
      
Try deleting a car record:

- use Postman:
![Delete Car by Postman](img/postman_delete_car.png)
  
### For Developers

If you would like to contribute to this project, make a new git branch and work on that branch. Make sure to commit the 
code to the new branch and make a pull request to merge to the main branch.

Before committing the code, always reformat the code. In Intellij IDEA, the formatting shortcut for Windows is **Ctrl+Alt+Shift+L**.
