# Login System

Sample App that uses Spring Boot for building a login system with registration and confirmation token email

### Uses
1. Spring Boot
2. Spring Data JPA
3. Spring Validation
4. Spring Security
5. Spring Mail
6. Spring DevTools
7. Lombok
8. BcryptPasswordEncoder
9. Mysql Connector Driver

### Build
```shell
mvn package
```

### Run
***with Spring Boot maven plugin:***
```shell
mvn spring-boot:run
```


***with executable jar:***
```shell
java -jar target/login-system.jar
```

#### TODO
1. [ ] Handle better validation and exception
2. [ ] Document with OpenAPI
3. [ ] Make some scripts for CI/CD pipelines
4. [ ] Add docker capability with Dockerfile and docker-compose
5. [ ] Improve configuration variables handling