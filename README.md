# DPH Digital-Pharma-Handbook
## Summary
A small university project to learn Test Driven Development and auxiliary coding softwares like:
- Maven
- Git, GitHub, Github-Actions
- Junit with AssertJ
- SWING GUI created with WindowBuilder and testing made with AssertJSwing
- SonarCloud / SonarQube Code Quality Checks
- Mockito
- Docker, locally, networked and with Docker compose
- MongoDB Java Driver
- TODO: add SQL DB software

## Project Description
The application is based on the phisical handbook that doctors, pharmacological experts or drug companies use to list all pathologies, 
diseases or phisical traumas (Conditions) and the associated drug and dosage to give to the patient.
The application is composed of three parts (packages): 
- Data Application Layer, responsible of the interactions between the application 
and the database of choice (MongoDB or TODO: add SQL DB software)
- Information Model, responsible of creating and maintaining an object model of the database information. 
Every change or update to the informations are first saved on theseobjects: when the application closes, the difference is saved and stored in the database.
-Application User Interface: responsible of the display of the GUI to the user, enabling CRUD operations on the object model of the database contents. 

## Instructions
TODO

## Maven instructions
TODO
