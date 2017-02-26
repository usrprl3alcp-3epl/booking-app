# Boardrooms reservation app #

There is application that let’s you create booking requests for meetings in boardrooms and retrieve successful booking calendar as output using [Spring Data REST](http://projects.spring.io/spring-data-rest/). 

Spring Data REST takes the features of [Spring HATEOAS](http://projects.spring.io/spring-hateoas/), [Spring Data JPA](http://projects.spring.io/spring-data-jpa/), [HAL](http://stateless.co/hal_specification.html) format for JSON output and combines them together automatically.

The app already deployed on AWS and accessible on [booking.us-west-2.elasticbeanstalk.com](http://booking.us-west-2.elasticbeanstalk.com)

## How do I get set up? ###

* Configuration

Java 8 runtime, Maven and MySql server are required

* Manual Database configuration

Required database schema and user provided by sql scripts **schema.sql, user.sql, drop.sql** which placed in **db_setup** directory, **dummy_data.sql** can be useful for testing and development goals.

* Auto Database configuration

Alternatively database schema can be created by hibernate automatically, take a look at **spring.jpa.hibernate.ddl-auto** property. Can be useful for development.

## Deployment instructions

No require any application server for deployment. There is embedded Tomcat servlet container, instead of deploying to an external instance.

By default app looks for **bookingdb** mysql database at localhost:3306 with credentials **bookinguser/bookingpass**. By the way, this can be configured in **application.properties**.  

## How to run

Build jar:

```bash
mvn clean install
```

Run tests:

```bash
mvn test
```

To start application run:

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar ./target/booking-{version}.jar
```