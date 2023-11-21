# MySQL CRUD with Spring Boot

This application server educational purposes as it is an academic project. It was based on an interview with an Endocrinologist, explaining the app's interface and funcionalities. 

## Installation

Please install all the dependencies needed to run the project, described in the pom.xml file.

As this is a Maven project you can run the following command to do so:
```bash
mvn clean install
```

## Database

This application runs over a MySQL localhost server, as described in the application.properties file.

Please, read the mentioned file and create a database accordingly.

## Database UML Modelling

In the repository, there are .png and .brM3 files in order to allow visualization of the process to get to the final database schema.

In order to open the .brM3 file, please visit [this](https://www.sis4.com/brModelo/download.html) link.

## How to Run

1. First, alter the application.properties file and set the username and password for your MySQL user
2. If you will, alter application.yml's port to one of your choosing
3. Run IEP-criacao.sql on your DBMS of choice so that the database is created
    1. If you want to, you can also run IEP-provoamento.sql to populate the database
5. Run IEPApplication.java

## Authors

This project has educational purposes and was created by [Henrique Cordeiro](https://www.linkedin.com/in/henrique-cordeiro-pereira/), [Eduarda Figueredo](https://www.linkedin.com/in/eduarda-souza-figueredo-293074232/) and [Adaury Oliveira](https://www.linkedin.com/in/adaury-neto/)
