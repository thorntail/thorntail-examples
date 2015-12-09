package org.wildfly.swarm.examples.scala

import java.util.Date
import javax.ws.rs.{GET, Path}

// @author Helio Frota
// @author Yoshimasa Tanabe
@Path("/")
class ScalaResource {

  @GET
  def get() = {
    "Hail at " + new Date()
  }
}
