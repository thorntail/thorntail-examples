# Multiple services plus NetFlixOSS Ribbon

> NOTE: This example will not work yet, as it relies upon a patch
> in the upstream of WildFly not yet available in a released version.

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
    $ mvn -Djboss.http.port=8081 wildfly-swarm:run

Maybe run it twice:

    $ cd time
    $ mvn -Djboss.http.port=8082 wildfly-swarm:run

Then run the events service, which consumes the time service(s):

    $ cd events
    $ mvn wildfly-swarm:run

Then

* http://localhost:8080/

Kill and restart one or both of the `time` services, and witness how stuff
behaves.
