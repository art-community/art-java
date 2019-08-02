/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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