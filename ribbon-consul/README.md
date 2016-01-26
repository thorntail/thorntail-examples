# Multiple services plus NetFlixOSS Ribbon

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/SWARM

This example is like the `ribbon` example, but instead of using
JGroups, it relies upon Hashicorp's Consul service-discovery mechanisms.

To execute this test, you must

* Have v2.0+ of phantomjs available in your $PATH
* Have a Consul server/agent running on localhost:8500

You can run consul with

    consul agent -dev

Once the above is satisfied, you can run

    mvn verify -Pconsul


