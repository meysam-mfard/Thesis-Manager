# Thesis Manager
A web application for managing thesis activities at a university.
It was done as a university course group project.

## Run
#### with h2 database
To run the application and load some sample data into the database:
```
java -jar -Dspring.profiles.active=productH2 thesismanager.jar
```

#### with MySQL
The SQL [scripts](./thesismanager/src/main/scripts) can be used to configure the database.

- To run the application and load sample data into the database(should not be used more than once):
```
java -jar -Dspring.profiles.active=productMysql-loadData thesismanager.jar
```

- To run without loading sample data or once it is loaded:
```
java -jar -Dspring.profiles.active=productMysql thesismanager.jar
```
##
The application is accessible via `http://localhost:8080/`
