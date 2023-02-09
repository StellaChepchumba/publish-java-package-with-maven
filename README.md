# Messaging and Notification

Imalipay messaging and notifiction modules 

## Features

### Email

This is to track and automate messages and notification send out from Imalipay via email channels between:

* Emails send out on demand synchronously 
* Emails send out on demand asynchronously
* Encrypting sensitive information as needed

### SMS

This is to track and automate messages and notification send out from Imalipay via sms channels between:

* SMS send out on demand synchronously 
* SMS send out on demand asynchronously
* Tracking the SMS sent out 
* Encrypting sensitive information as needed
* Support for multiple providers 
* Routing to different providers as needed

		
### Whatsapp

This is to track and automate messages and notification send out from Imalipay via Whatsapp channels between:

* Whatsapp send out on demand synchronously 
* Whatsapp send out on demand asynchronously
* Tracking the Whatsapp sent out 
* Encrypting sensitive information as needed
* Support for multiple providers 
* Routing to different providers as needed
		
### Push Notification

This is to track and automate messages and notification send out from Imalipay via Push notification channels between:

* Push notification send out on demand synchronously 
* Push notification send out on demand asynchronously
* Tracking the Push notification sent out 
* Encrypting sensitive information as needed
* Support for multiple providers 
* Routing to different providers as needed
		

## Getting Started

See below for a short technical introduction of the module. More to find in [These instructions](doc/GETTING_STARTED.md). The instruction below will surely get you a copy of the project up and running on your local machine for development and testing purposes. 

### Dependencies

Ledger is heavily dependents on spring. We are still working on a more inclusive dependency management.

### Building and Running
<!---
```
	> git clone https://github.com/adorsys/ledgers.git
	> cd ledgers
	> mvn clean install
	> cd ledgers-app
	> mvn spring-boot:run -Dspring.profiles.active=h2
```

This will start the ledgers app with the embedded h2 database.

### Visiting the API

[http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html#/)


### Visiting the Database

when started with the h2 profile, you can use the web browser to visit database tables on the url [http://localhost:8088/h2-console/](http://localhost:8088/h2-console/) . make sure you use the following connection properties:

Driver Class : org.h2.Driver
JDBC URL: jdbc:h2:mem:ledgers
User Name: sa
Password: sa

Press Connect button and you can explore the data model.

More on this to come...
--->

## Architecture 

Available in [the documentation](doc/architecture.md)

## Deployment

## Module Structure 
We user Spring dependencies to assemble module. Each module contains following dependency management.
The payments application is built to be fully extensible

## Development and Contributing


## Built with 

* Java, version 11 - The main language of implementation
* Maven, version 3.0 - Dependency Management
* Spring Boot - Spring boot as core Java framework

## Release notes

* [Release notes](doc/architecture/release-notes/releasenotes.md)  

## Roadmap

* [Roadmap](doc/roadmap.md) - The up-to-date project's roadmap

## Authors 

* **[Freeman Kuguyo](mailto:freeman@imalipay.com)**