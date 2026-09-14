package httpclient

object Main {
  def main(args: Array[String]): Unit = {
    val response = HttpClient.get("httpbin.org", "/get")
    val body = HttpClient.readBody(response)

    Console.println(s"Status: ${response.status()}")
    Console.println(body.take(200))
  }
}
