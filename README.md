# GearMax-API

GearMax is a web application that helps buyers find the best-fit cars, assists sellers to post their used cars and
provides price prediction functionalities.

For a lot of reasons such as improving the scalability, we separate the web app into three services: backend, frontend
and database. The code of the three services is managed in three git repositories. This project, **GearMax-API** is
responsible for providing the backend service. Specifically, this is a Spring Boot backend project that provides REST
APIs to deliver web services.

### Functionality Requirements

Before the reading week, GearMax web app is expected to have the following functionalities:

- Users will see the main page when they input the url of our website.
- The navigation bar on the main page can direct them to the search page.
- Users can search posts of their interested used cars by filters, and the filters include:
    - price range ([]-[], e.g., 1000-2000)
    - bodyType
      `[suv-crossover, sedan, coupe, hatchback, pickup-truck, wagon, minivan, van, convertible]`
    - make name/brands
      `[ford, chevrolet, toyota, nissan, honda, jeep, hyundai, kia, ram, gmc, dodge, volkswagen, subaru, mercedes-benz, buick, bmw, mazda, cadillac, lexus, chrysler, audi, lincoln, acura, infiniti, volvo, mitsubishi, land-rover, porsche, mini, jaguar, alfa-romeo, genesis, maserati, fiat, pontiac, scion, tesla, mercury, saturn, ferrari, ...]`
    - year range ([]-[], e.g., 2010-2012)
    - mileage ([x], less than or equal to x)
    - listing color/exterior color
      `[black, blue, brown, gold, gray, green, orange, pink, purple, red, silver, teal, white, yellow]`
    - maximum seating ([x], less than or equal to x)
    - transmission display
      `[automatic, continuously-variable-transmission, 6-speed-automatic, 8-speed-automatic, 9-speed-automatic, 6-speed-automatic-overdrive, 7-speed-automatic, 5-speed-automatic, 4-speed-automatic, 6-speed-manual, manual, 8-speed-automatic-overdrive, 5-speed-automatic-overdrive]`
    - options/features (xxx+yyy+zzz, for example, bluetooth+backup-camera)
      `[Backup Camera, Bluetooth, Alloy Wheels, Heated Seats, Remote Start, CarPlay, Android Auto, Navigation System, Blind Spot Monitoring, Leather Seats, Alloy Wheels, Parking Sensors, Sunroof/Moonroof, Adaptive Cruise Control, Third Row Seating, Quick Order Package, Convenience Package, Premium Package, Steel Wheels, Tow Package, Appearance Package, Steel Wheels, Technology Package, Multi Zone Climate Control, Preferred Package, Backup Camera, Power Package, Cold Weather Package, Heat Package, Off Road Package, Premium Wheels, Adaptive Suspension, Trailer Package, Sport Package, Cargo Package, Chrome Wheels, Suspension Package, SE Package, Comfort Package, Memory Package, ...]`
- When users input a wrong url, our website will always direct them to the search page.
- Users can view the detailed information of a used car when they click a specific post.

After the reading week, GearMax app is expected to have the following functionalities:

- Users can sign in/log in to GearMax.
- Signed users can post their used cars.
- GearMax can predict the future price of used cars by users' given information.

### Performance Requirements

- When users request a query/search, the response/search results must be returned within 1s.
- We assume that the users will not concurrently send requests, so we do not make our project a distributed system.

### The 12-Factor Methodology

GearMax follows the 12-factor methodology, which is the best practice for SaaS development, to develop applications that
run as services.

For more explanations:

[What is the Twelve-Factor App?](https://12factor.net/)

[Twelve-Factor Methodology in a Spring Boot Microservice](https://www.baeldung.com/spring-boot-12-factor)

### Technologies

The technologies that the project uses:

- Spring Boot
- React (gearmax-react-app)
- Spring JPA
- MySQL
- Elasticsearch
- Redis
- Docker

### About React (Frontend)

Please see [repo](https://github.com/gearmax-uw/gearmax-react-app) of GearMax-React-App.

### About Data

Please see [repo](https://github.com/gearmax-uw/gearmax-data) of GearMax-Data.

---
**The contents below is for GearMax-API specifically.**

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
         |
         +- config
         |
         +- query
         |
         +- serializer
         |
         +- util

The *controller* package includes classes annotated as *@Controller*, which handles HTTP requests. Within each request
function, you can invoke *service* functions but do not directly invoke *repository* functions.

The *service* package includes interfaces that defines basic behaviors of web services. The *service/impl/* package
includes classes annotated as *@Service*, which implements the corresponding interfaces and defines the concrete
functions. For example, interface *CarService* specifies the behavior *saveCar()*, then there must be a class titled *
CarServiceImpl*
to implement this function. In each service function, you can invoke *repository* functions to deal with real data.

The *repository* package includes interfaces that extends *CrudRepository* or *JpaRepository*. These interfaces
inherently implements SQL functions to add/delete/update data, and what we have to do is just calling those APIs such
as `carRepository.save(car)`
in service level.

The *domain* package includes classes annotated as *@Entity*, which are POJOs representing data that can be persisted to
the database. An entity represents a table; for instance, the entity class *Car* represents a table called *Car*. If you
create a new *Car* object and store the object via `carRepository.save(car)`, then a record will be stored in the *Car*
table.

The *controller/viewobject* includes classes which represents view objects. Those classes are very similar to entity
classes, but they are wrapped to only have attributes expected to be displayed to users/developers. In controllers, we
only return view objects rather than entity objects.

### CI/CD Process

![CI/CD](/img/gearmax-cicd.png)

### Environment Requirements

To run this project locally, your environments must meet the following criteria:

- Installed JDK (at least 11) and Maven
- Redis installed
- MySQL Server 8.0+
- Elasticsearch 7.6.2
- Docker && Docker Compose

### How to configure this app in either dev or prod environment?

If you would like to run this app in dev (development) environment, go to *
gearmax-api/src/main/resources/application.properties*
and specify `spring.profiles.active=dev`. This specification will deploy the configurations in *
application-dev.properties* when you start your app.

If you would like to run this app in prod (production) environment, find *application.properties* and specify
`spring.profiles.active=prod`. This specification will deploy the configurations in *application-prod.properties* when
you start your app.

#### How to configure this app in prod environment?

If you would like to change the production environment settings/configurations, find *
gearmax-api/src/main/resources/application-prod.properties*
and update the settings. Make sure the configurations to connect to Elasticsearch/MySQL/Redis match your case;
otherwise, the app may not work properly. The same requirements apply to dev environment.

To avoid leaking sensitive information, we use [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot) to do the
password encryption. Jasypt allows you to only specify the encrypted password in the *application.properties* file. For
example, if you would like to encrypt a value *'InfoToBeEncrypted'* using the salt *'test'*, you can run
command `mvn jasypt:decrypt-value -Djasypt.encryptor.password="test" -Djasypt.plugin.value="InfoToBeEncrypted"`. You
will see the output encrypted value *ENC(/nJHoNctIHpmuaGcmqqUIt5EPw/3/CWzz7RVZrOdhof3NnyvMezwO84n+WdESXLu)*, so you can
replace the old original password with the encrypted value. Also, do not forget to specify your salt in the property
file as well: `jasypt.encryptor.password=test`.

### How to run this app?

- Go to *gearmax-api* and run `mvn clean install`, and you will find the newly generated `targets/gearmax-api.jar`.
- Put the jar file to some location, and create a bash shell script, let's say `deploy.sh`.
- Put the command to the
  script: `nohup java -Xms400m -Xmx400m -XX:NewSize=200m -XX:MaxNewSize=200m -jar gearmax-api.jar`.
- Run `chmod 777 deploy.sh` to give the execution permission.
- Run `./deploy.sh`, you will see the api project is starting and running (if your configuration in property file is
  correct).

### How to send requests after the app starts?

You can directly send http requests or use Postman. Either way is fine.

**Try searching cars by given parameters**:

To get list of car posts, you should send GET HTTP request: `http://${host}:${port}/car/list?${parameters}`

In our case, `host=34.125.152.171:8080`, `port=8080`. Hence, without parameters given, the url should
be `http://34.125.152.171:8080/car/list`.

For the parameters:

- filters (optional):
    - price: `price=${minPrice}-${maxPrice}, e.g, price=10000-100000`
    - bodyType: `bodyType=${bodyType}, e.g, bodyType=suv-crossover`
    - makeName: `makeName=${makeName}, e.g., makeName=mercedes-benz, makeName=kia`
    - year: `year=${minYear}-${maxYear}`
    - mileage: `mileage=${maxMileage}, e.g., mileage=100000`
    - listingColor: `listingColor=${listingColor}, e.g., listingColor=black`
    - maximumSeating: `maximumSeating=${maxSeating}, e.g., maximumSeating=10`
    - transmissionDisplay: `transmissionDisplay=${transmissionDisplay}, e.g, transmissionDisplay=7-speed-automatic`
    - features: `features=${f1}+${f2}+${f3}+..., e.g, features=leather-seats+bluetooth+alloy-wheels`

- pagination (optional):
    - pageIndex: page number, 0 in default, `pageIndex=${pageIndex}, e.g, pageIndex=1`
    - pageSize: page size, 10 in default, means 10 items (car posts) to be shown on this
      page `pageSize=${pageSize}, e.g., pageSize=20`
    - sort: sort field, can only be `price` or `year` so far. `sort=price` or `sort=year`.
    - sortOrder: sort order, can only be `asc` or `desc`. `sortOrder=asc` or `sortOrder=desc`.

The parameters are concatenated by `&`. The examples GET HTTP requests include:

    http://34.125.152.171:8080/car/list?makeName=land-rover&price=10000-100000&bodyType=coupe&maximumSeating=10&yearRange=2000-2020&mileage=100000&features=leather-seats+bluetooth+alloy-wheels
    http://34.125.152.171:8080/car/list?makeName=land-rover&pageIndex=1&pageSize=20&sort=price&sortOrder=desc    

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

Before committing the code, always reformat the code. In Intellij IDEA, the formatting shortcut for Windows is **
Ctrl+Alt+Shift+L**.

Also, we recommend using SonarLint to fix your code quality issues before your committing the code. Here is
the [link](https://www.sonarlint.org/)
of SonarLint IDE extensions.
