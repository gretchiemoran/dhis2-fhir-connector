# DHIS2 FHIR Location Connector
 
This application provides a FHIR server that will serve DHIS2 organization unit data as FHIR Location resources. The source was heavily influenced by UiO's dhis2-fhir-adapter (https://github.com/dhis2/dhis2-fhir-adapter/blob/master/README.md) project, which imports data into DHIS2 using FHIR subscriptions. 
 
The application is a proof-of-concept built to demonstrate an easy-to-deploy external service providing FHIR compatible delivery of DHIS2 assets, necessary for a standards based approach to system interoperability with DHIS2. 

__The connector is still under development and the repository contains the development source code.__  No unit testing is in place, and very few checks are being made for error handling. As proof-of-concept, there are no guarantees this work will be completed. 

The use case for the connector is as such: 

The connector war may be deployed beside DHIS2 (as an embedded executable in the simplest scenario) and automatically a FHIR server is available to serve DHIS2 data in FHIR compatible format. Currently, we are prototyping with organization unit (DHIS2) to Location resource (FHIR) mapping only. 

## Data Mapping

This connector responds to requests for Location resources from the FHIR server. When the request comes in, the data is queried from DHIS2 using DHIS2 metadata api hooks, then mapped from the DHIS2 data model to the FHIR data model. 

|  FHIR Structure | DHIS2 Metadata Entity | DHIS2 Metadata Attribute | Derived? | Required  |
|  ------ | ------ | ------ | ------ | ------ |
|  Location.resourceType |  |  | Location | yes |
|  Location.id |  |  | derived unique id | yes |
|  Location.status |  |  | active | no |
|  Location.mode |  |  | instance | no |
|  Location.identifier.system |  |  | http://dhis2.org/code | no |
|  Location.identifier.value | organisationUnit | code |  | no |
|  Location.identifier.system |  |  | http://dhis2.org/id | no |
|  Location.identifier.value | organisationUnit | id |  | no |
|  Location.name | organisationUnit | name |  | no |
|  Location.alias | organisationUnit | shortName |  | no |
|  Location.physicalType.coding.system |  |  | http://hl7.org/fhir/location-physical-type | no |
|  Location.physicalType.coding.code |  |  | area:bu | no |
|  Location.physicalType.coding.display |  |  | Area:Building | no |
|  Location.physicalType.text |  |  | Administrative Area:Facility | no |
|  Location.position.longitude | organisationUnit | coordinates |  | yes |
|  Location.position.latitude | organisationUnit | coordinates |  | yes |
|  Location.partOf.reference | organisationUnit | parent.id | some derivation necessary: second to last element in path | no |

## Running the Connector in Development Environment
### Dependent Software Components
#### DHIS2

DHIS 2.29 must be installed and the DHIS2 demo DB with data for Sierra Leone can be used. No additional setup is required for the connector when using this data.

In order to use the connector without any configuration change, DHIS2 Web API Endpoints should be accessible on http://localhost:8085/api.

The following GitHub repo has a sufficiently straightforward Docker Compose file for standing up this DHIS2 configuration.
```
https://github.com/pgracio/dhis2-docker
```

To change the connection parameters to DHIS2 for deployment, modify the properties in the pom.xml in the root of the /app folder. 

```
<properties>
    ...
    <dhis2.username>admin</dhis2.username>
    <dhis2.password>district</dhis2.password>
    <dhis2.url>http://localhost:8085</dhis2.url>
    <dhis2.apiVersion>29</dhis2.apiVersion>
  </properties>
```

To change the connection parameters to DHIS2 for successful unit testing, application-test.yml file in the /dhis/src/test/resources folder. 

```
...

dhis2.fhir-adapter:
  endpoint:
    url: http://localhost:8085/
    api-version: 29
    system-authentication:
      username: admin
      password: district
```

### Build and Deploy
#### Configure

The connector defaults to run on localhost at port 8083. 

To change these defaults, modify the properties in the pom.xml in the root of the /app folder. 

```
<properties>
    <application.port>8083</application.port>
    ...
  </properties>
```

To change the defaults for successful unit testing, application-test.yml file in the /dhis/src/test/resources folder. 

```
server:
  port: 8083

... 
```
#### Build

In order to build the adapter Java Development Kit 8 and Maven 3.2 or later is required. No additional repositories need to be configured in Maven configuration. The following command builds the artifact dhis2-fhir-connector.war in sub-directory app/target. Run this command in the root of the project directory.

    mvn clean install

The project can also be imported into an IDE like IntelliJ IDEA ULTIMATE or Eclipse where it can be built automatically.

### Deploy

The connector WAR can be run with a servlet container 3.1 or later (like Apache Tomcat 8.5 or Jetty 9.3). In an IDE also class org.dhis2.fhir.adapter.App can be used to start the connector as Spring Boot application without an external servlet 
container.

After successfully building the application Maven can be used to run the application. Enter the following command in folder app in the console:

    mvn spring-boot:run
    
Since the created WAR file is an executable WAR file (can also be disabled when building), also the following command can be entered in folder app/target in the console (recommended for deployment):

    java -jar dhis2-fhir-connector-exec.war 

### Testing the Endpoints

The two endpoints available are:

1. looking up an organization unit by ID, 
2. and searching all organization units (which will return all org units in the DHIS2 instance as FHIR Locations). 

#### Examples 

Retrieve an organization unit by ID: 

http://localhost:8083/fhir/Location/DQHGtTGOP6b?_pretty=true&_format=json

Search for all organization units: 

http://localhost:8083/fhir/Location?_pretty=true&_format=json

 


