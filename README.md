# cassmig
Cassadra java schema migrator that uses the datastax driver and requires a datastax Session
##Maven dependency
```
<dependency>
    <groupId>com.github.ecyshor</groupId>
    <artifactId>cassandra-migration</artifactId>
    <version>1.0.1</version>
</dependency>
```
##Migration files
The migration files are of two types:
####Initialization file:
This contains all the cql queries that must be executed before creating the migrations table.
Only one if these files must be configured.
It has the following format:
```
--keyspace=marguerita                     #the keyspace where the migrations table will be executed
--migration_init                          #marks that this is the initilization file
--description=Initial keyspace creation   #description of the migration file
--start                                   #marks the start of the migrations queries
    CREATE KEYSPACE marguerita WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
--end                                     #marks the end of the migrations queries
```

All the keys presented are required.
####Migration files:
These contain the actual migrations:
```
--order=1                                 #The order of the migration file. Files with lower order will be applied first. If there are two files with the same order one will execute before the other. You cannot exactly tell which one will execute first but no matter how many time it will be executed, it will always be executed first. This must be a positive integer.
--keyspace=marguerita                     #The keyspace where the migration will run. This will cause the first command the be executed to be `USE marguerita`
--description=Create the first table      #description of the migration file
--start                                   #marks the start of the migrations queries

CREATE TABLE drinks (
  salt bigint,
  column uuid,
  email varchar,
  PRIMARY KEY (salt, email)
);

--end                                     #marks the end of the migrations queries
```
We can have any number of normal migration files

##Usage

###To migrate using files found in your project/module 
```
CassandraMigrator migrator = new CassandraMigrator(session);
migrator.migrate("migrations");
```

The "migrations" folder must be in the classpath, preferably resources folder. All the migration files must be there.
The required session is of type `com.datastax.driver.core.Session;`

###To migrate using files found in external modules/dependencies
```
CassandraMigrator migrator = new CassandraMigrator(session);
migrator.migrateExternal("com.example","migrations");
```

 - "migrations" represents the prefix for the migrations files that should be searched. If in an external dependency,
 this should be the folder where the files are located. Please take note that this should be the `prefix`
 - "com.example" represents the packages prefix for libraries where we should scan for files. 
