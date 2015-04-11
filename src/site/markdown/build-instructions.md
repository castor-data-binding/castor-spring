# Build instructions

Instructions for building this project

## Prerequisites

In order to build the Sping ORM module for Castor JDO, you will have 
the following requirements met on your system:
        
* Download and install [Maven](http://maven.apache.org)
* Download and install a git client
* Download and install {Spring](http://www.springframework.org)
        
As this project uses Maven for build and deployment, all required compile-time dependencies will automatically be resolved by Maven
and deployed into your local Maven repository.
           
## Building the Spring ORM module
        
This section describes how to build the Spring module from a command line using Maven. 

*Note*: Please note that you will have to download several JARs for SUN APIs like JTA and the Connector API yourself and 
manually inject these JARs into your Maven repository. For a more complete explanation of the problem, please check
[here](http://maven.apache.org/guides/mini/guide-coping-with-sun-jars.html)
           
This section assumes that you have ckecked out the latest sources from the github repo for the Spring ORM module for Castor JDO.
Instructions for doing so are provided [here](source-repository.html).
           
Open a command line on your system, and issue the following commands:
           
```
 > mvn package            
```
        
Above command will compile the sources and create the distribution JAR.
           
To create the complete project documentation - in addition to the distribution assembly, please issue ...
           
```
 > mvn site            
```
        
To install the newly created distribution JAR into your local Maven repository, please issue the following command:
           
```
 > mvn install            
```
        
## Examples
        
For examples how to use the Spring ORM module for Castor JDO, please
check the following [document](data-access-with-spring.html).
           
