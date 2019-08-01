package request

object Request {
  val CONNECTION_REQUEST: String => String = (userId: String) =>
    s"""
       |{
       |	"profile": "$userId",
       |	"modulePath": "test",
       |	"moduleEndpoint": {
       |		"host": "localhost",
       |		"port": 1234
       |	}
       |}
    """.stripMargin

  val GET_CLUSTER_PROFILE_REQUEST: String => String = (userId: String) =>
    s"""
       |{
       |	"profile": "$userId"
       |}
    """.stripMargin
}
