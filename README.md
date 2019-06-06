# Thesis Manager
A web application for managing thesis activities at a university.
It was done as a university course group project.

## Run
#### with h2 database
To run the application and load the sample data into the database, activate profile `productH2` or `default`.

#### with MySQL
The SQL [scripts](./thesismanager/src/main/scripts) can be used to configure the database.

- To run the application and load the sample data into the database, activate profile `productMysql-loadData`
(should not be activated more than once).

- To run without loading the sample data or once it is loaded, activate profile `productMysql`.

##
The application is accessible via `http://localhost:8080/`.
