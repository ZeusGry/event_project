# Event_App

It is an event management application.  
It allows you to:  
* register a new user  
* log in  
* add an event (you can add additional organizers by entering their e-mail)  
* commenting the event 
* selecting if you are participating in the event  

Login is done on the basis of JWT.  
All data is saved in the database.  
When adding additional organizers who are not users, he sends them a special e-mail for registration.

###Frontend for aplication:
[Frontend](https://github.com/ZeusGry/EventFrontend)

###application.properties:
spring.datasource.url=  
spring.datasource.password=
spring.datasource.username=
spring.jpa.hibernate.ddl-auto=

spring.mail.host=  
spring.mail.port=  
spring.mail.username=  
spring.mail.password=
spring.mail.properties.mail.smtp.auth=  
spring.mail.properties.mail.smtp.starttls.enable=  

spring.jpa.show-sql=  
jwt.secret=  
jwt.time=  