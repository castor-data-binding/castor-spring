# Usage

Instructions for integrating Spring ORM for Castor JDO with your projects

# Getting started using Maven

Please add the following dependency to include a released version of the Spring ORM package for
Castor JDO with your project:

```
<dependency>
   <groupId>org.codehaus.castor</groupId>
   <artifactId>spring-orm</artifactId>
   <version>1.1</version>
</dependency>
```

If you create a dependency against a SNAPSHOT release, you will
have to add the following <tt>&lt;repository&gt;</tt> element to your POM as well,
so that Maven 2 knows about the <i>Codehaus Snapshot repository</i> when trying
to resolve and download dependencies.

```
<repository>
    <id>codehaus-snapshots</id>
    <name>Maven Codehaus Snapshots</name>
    <url>http://snapshots.maven.codehaus.org/maven2/</url>
</repository>
```
