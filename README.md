# Boardrooms reservation app #

It is application that let you create booking requests for meetings in boardrooms and retrieve successful booking calendar as output using [Spring Data REST](http://projects.spring.io/spring-data-rest/).

Booking calendar based on submitted reservations and contains bookings which grouped chronologically by day.

There are several restrictions:
* Room's working time
* Overlapping with existing reservations
* Validation

Spring Data REST takes the features of [Spring HATEOAS](http://projects.spring.io/spring-hateoas/), [Spring Data JPA](http://projects.spring.io/spring-data-jpa/), [HAL](http://stateless.co/hal_specification.html) format for JSON output and combines them together automatically.

The app already deployed on AWS and accessible on [ec2-54-213-113-217.us-west-2.compute.amazonaws.com](http://ec2-54-213-113-217.us-west-2.compute.amazonaws.com)

## How do I get set up? ###

* Configuration

   Java 8 runtime, Maven and MySql server are required
   
* Manual Database configuration

   Required database schema and user provided by sql scripts `schema.sql`, `user.sql`, `drop.sql` which placed in `db_setup` directory, `dummy_data.sql` can be useful for testing and development purposes.

* Auto Database configuration

   Alternatively database schema can be created by hibernate automatically, take a look at **spring.jpa.hibernate.ddl-auto** property. Can be useful for development.

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

## Deployment instructions

The app no require any additional application server for deployment. There is embedded Tomcat servlet container, instead of deploying to an external instance.

By default app looks for `bookingdb` mysql database at `localhost:3306` with credentials `bookinguser/bookingpass`. By the way, this can be configured in `application.properties`.  

## Test the application manually

Now that the application is running, you can test it. You can use any REST client you wish.

First you want to see the top level service.

```bash
$ curl http://localhost:8080
{
  "links" : [ {
    "rel" : "reservations",
    "href" : "http://localhost:8080/reservations{?page,size,sort}"
  }, {
    "rel" : "employees",
    "href" : "http://localhost:8080/employees{?page,size,sort}"
  }, {
    "rel" : "rooms",
    "href" : "http://localhost:8080/rooms{?page,size,sort}"
  }, {
    "rel" : "calendars",
    "href" : "http://localhost:8080/calendars"
  }, {
    "rel" : "profile",
    "href" : "http://localhost:8080/profile"
  } ]
}
```

Here you get a first glimpse of what this server has to offer. There are several links for REST resources. Some of them has some options such as `?page`, `?size`, and `?sort`. As was mentioned above Spring Data REST uses the [HAL](http://stateless.co/hal_specification.html) format for JSON output.

### Employee REST testing

Let's fetch all available employees:

```bash
$ curl http://localhost:8080/employees
{
  "links" : [ {
    "rel" : "self",
    "href" : "http://localhost:8080/employees"
  }, {
    "rel" : "profile",
    "href" : "http://localhost:8080/profile/employees"
  } ],
  "content" : [ {
    "value" : [ ],
    "rel" : null,
    "relTargetType" : "com.assignment.domain.Employee",
    "collectionValue" : true
  } ],
  "page" : {
    "size" : 20,
    "totalElements" : 0,
    "totalPages" : 0,
    "number" : 0
  }
}
```

There are currently no elements and hence no pages. Time to create a new Employee!

Sample payload for `POST http://localhost:8080/employees`

```json
{
  "firstName" : "John",
  "lastName" : "Smith",
  "telephone" : "+02938451345",
  "email" : "John@mail.com"
}
```

and the response is

```bash
201 CREATED
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "telephone": "+02938451345",
  "email": "John@mail.com",
  "content": [],
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8080/employees/1"
    },
    {
      "rel": "employee",
      "href": "http://localhost:8080/employees/1"
    }
  ]
}
```

and then there is one employee in the list of employees:

```bash
$ curl http://localhost:8080/employees
{
  "links" : [ {
    "rel" : "self",
    "href" : "http://localhost:8080/employees"
  }, {
    "rel" : "profile",
    "href" : "http://localhost:8080/profile/employees"
  } ],
  "content" : [ {
    "id" : 1,
    "firstName" : "John",
    "lastName" : "Smith",
    "telephone" : "+02938451345",
    "email" : "John@mail.com",
    "content" : [ ],
    "links" : [ {
      "rel" : "self",
      "href" : "http://localhost:8080/employees/1"
    }, {
      "rel" : "employee",
      "href" : "http://localhost:8080/employees/1"
    } ]
  } ],
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
```

There is possible validation error with *400 Bad request* response. Let's attempt to create employee without `firstName`:

```
POST localhost:8080/employees
{
  "lastName" : "Smith",
  "telephone" : "+02938451345",
  "email" : "John@mail.com"
}
```

```
400 BAD REQUEST
{
  "error": [
    "Employee's first name cannot be null"
  ]
}
```


Http methods *GET*, *POST*, *PUT*, *DELETE* are supported.
The similar behaviour also implemented for `Room` and `Reservatin`' models.

### Room REST testing

Sample payload for `POST http://localhost:8080/rooms`

```json
{
  "name" : "Bahamas room",
  "code" : "5232C",
  "startTime" : "09:00:00",
  "endTime" : "22:00:00"
}
```
and the response is 201 Created

```json
{
  "id": 1,
  "name": "Bahamas room",
  "code": "5232C",
  "startTime": "09:00:00",
  "endTime": "22:00:00",
  "content": [],
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8080/rooms/1"
    },
    {
      "rel": "room",
      "href": "http://localhost:8080/rooms/1"
    }
  ]
}
```
### Reservation REST testing
Sample payload for `POST http://localhost:8080/reservations`

```json
{
  "startDate" : "1125-12-22T11:20:02",
  "duration" : "00:30:00",
  "employee" : "http://localhost:8080/employees/1",
  "room" : "http://localhost:8080/rooms/1"
}
```

and the response is 201 Created

```json
{
  "id": 1,
  "startDate": "1125-12-22T11:20:02",
  "duration": "00:30:00",
  "submissionDate": "2017-02-27T00:19:21.002",
  "endDate": "1125-12-22T11:50:02",
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8080/reservations/1"
    },
    {
      "rel": "room",
      "href": "http://localhost:8080/rooms/1"
    },
    {
      "rel": "employee",
      "href": "http://localhost:8080/employees/1"
    }
  ]
}
```

but there are possible several errors according to restrictions:
* Room's working time
```
POST
{
  "startDate" : "1125-12-22T01:20:02",
  "duration" : "00:30:00",
  "employee" : "http://localhost:8080/employees/1",
  "room" : "http://localhost:8080/rooms/1"
}
```
```
409 CONFLICT
{
  "error": "Reservation dates don't fit room working time"
}
```
* Overlapping with existing reservations

```
POST
{
  "startDate" : "1125-12-22T11:20:02",
  "duration" : "00:30:00",
  "employee" : "http://localhost:8080/employees/1",
  "room" : "http://localhost:8080/rooms/1"
}
```
```
409 CONFLICT
{
  "error": "New reservation overlaps with existing reservation"
}
```

* Validation's error *400 Bad request* is also present:

```
POST
{
  "startDate" : "1125-12-22T11:20:02",
  "duration" : "00:30:00",
  "room" : "http://localhost:8080/rooms/1"
}
```

```
400 BAD REQUEST
{
  "error": [
    "Reservation's owner cannot be null"
  ]
}
```

### Booking Calendar testing

The above stuff is basis, but the major feature of the app is *Booking Calendar* as output. Booking calendar based on submitted reservations and contains bookings which grouped chronologically by day.

Let's get booking calendar for first room

```
GET http://localhost:8080/calendars/1
```

<img src="https://cloud.githubusercontent.com/assets/6156085/23343525/1b565116-fc7e-11e6-87c9-8b8a4200e888.png" width="500">

And get all calendars

```
GET http://localhost:8080/calendars
```

<img src="https://cloud.githubusercontent.com/assets/6156085/23344094/77797fd2-fc87-11e6-9728-68c98717d47f.png" width="500">

For *Booking Calendar* only *GET* methods are available, cause *Booking Calendar* based on submitted reservations.