# Multiple services plus NetFlixOSS Ribbon

> Please raise any issues found with this example in this repo:
> https://github.com/wildfly-swarm/wildfly-swarm-examples
>
> Issues related to WildFly Swarm core should be raised in the main repo:
> https://github.com/wildfly-swarm/wildfly-swarm/issues

The beginnings of a multi-service example.

Two services exist:

* Time
* Events

The `time` service simply returns the current time as a JSON map
with fields for hour, minute, second, etc.

The `events` service queries the `time` service, and returns a list of
currently on-going events.  Currently, it just generates a list of events
that started at the top of the current hour.

Build and run the time service:

  $ cd time
  $ mvn -Djboss.http.port=8181 wildfly-swarm:run

In a separate window

  $ cd events
  $ mvn wildfly-swarm:run

Then

* http://localhost:8080/
