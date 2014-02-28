package io.gatling.karaf.test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SimulationWithFollowRedirect extends Simulation {
  val scn = scenario("My scenario").exec(http("My Page").get("http://mywebsite.com/page.html"))
  setUp(scn.inject(atOnceUsers(1)))
}
