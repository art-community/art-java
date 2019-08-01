package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import request.Request.{CONNECTION_REQUEST, GET_CLUSTER_PROFILE_REQUEST}

import scala.concurrent.duration.DurationLong
import scala.language.postfixOps

class StateSimulation extends Simulation {
  setUp {
    (scenario("State simple load test")
      exec (http("connect") post "/connect" body StringBody(session => CONNECTION_REQUEST(session.userId.toString)))
      exec (http("getClusterProfile") get "/getClusterProfile" body StringBody(session => GET_CLUSTER_PROFILE_REQUEST(session.userId.toString)))
      inject(nothingFor(1 second), rampUsersPerSec(100) to 500 during (30 second) randomized)
      protocols (http baseUrl "http://localhost:1234/state" doNotTrackHeader "1"))
  }

}