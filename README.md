# OOP-Project
A codebase for an OOP project.

The project uses Maven for automating package management of the JavaFX UI library used in the project.

If you do not wish to install Maven in your system, look over to the `target/` directory and run the generated .jar file of the latest compiled version of the project.
```bash
java -jar target/HotelSystem-1.0-SNAPSHOT.jar
```

# Maven

(All the commands below must be executed in the root directory)

Run with Maven command in the root directory:
```bash
mvn compile exec:java -Dexec.mainClass="HotelManagerGUI"
```
Creating .jar:
```bash
mvn clean package
```

Then run (on the pom.xml for the namings):
```bash
java -jar target/HotelSystem-1.0-SNAPSHOT.jar
```
