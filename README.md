# bnote-server

Sample Note Backend Service in Spring Boot (Web, JPA, H2, DevTools dependencies) for Practice

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `io.brusser.bnoteserver.BnoteServerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

REST API will run on localhost:8080/v1/note see [swagger.yaml](https://github.com/brusser/bnote-server/blob/master/swagger.yaml) for API documentation

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/brusser/bnote-server/blob/master/LICENSE) file.
