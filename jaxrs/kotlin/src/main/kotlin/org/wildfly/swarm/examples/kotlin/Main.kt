package org.wildfly.swarm.examples.kotlin

import org.jboss.shrinkwrap.api.ShrinkWrap
import org.wildfly.swarm.container.Container
import org.wildfly.swarm.jaxrs.JAXRSArchive

//@author Helio Frota
fun main(args: Array<String>) {

    val container = Container()

    val deployment = ShrinkWrap.create(JAXRSArchive::class.java, "example-kotlin.war")
    deployment.addClass(KotlinResource::class.java)
    deployment.addAllDependencies()
    container.start().deploy(deployment)
}