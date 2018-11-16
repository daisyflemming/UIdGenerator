# createBatchId

This is a Spring Boot Rest Service that provision a sequential 4-6 characters id one at a time. It is tested on Java 8.
/getNextId                    # get the next id in memory
/getNextId/{characterLength}  # where  3 < characterLength <=6
/reset                        # reset input to 0, this is a convenient api and should not be exposed in production
/reset/{input}                # reset input to value specified

recording: https://photos.app.goo.gl/CDPB7BYELab3BtpbA

Assumption: 
* the maximum no. of id that can be generated are 21 x 36^5
* regardless the length of character required, the id would be drawn from the same pool

How to build the project in Intellij
- git clone or download this project to your mac
- select "Import Project" from Intellij Welcome page
- locate the directory "createBatchId" and select pom.xml to create the maven project
- click at the "Maven Projects" on the right vertical bar
- open Plugins > open Spring-boot > double click on sprint-boot:run to start the service at port 8080
- open http://localhost:8080/getNextId in a browser

How to build the project in linux
- git clone or download this project
- inside the directory createBatchId type "./mvnw spring-boot:run" at prompt to start the servie at port 8080
- open http://localhost:8080/getNextId in a browser

Reference: 
https://spring.io/guides/gs/rest-service/


