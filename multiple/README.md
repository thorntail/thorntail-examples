# Multiple services plus NetFlixOSS Ribbon

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

The beginnings of a multi-service example.

Build and run the time service:

  $ cd time
  $ mvn -Djboss.http.port=8181 wildfly-swarm:run

In a separate window

  $ cd events
  $ mvn wildfly-swarm:run

Then

* http://localhost:8080/
