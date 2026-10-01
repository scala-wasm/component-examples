package httpclient

import io.github.scalawasm.wasi4s.wasi.cli.stdout._

object Main {
  def main(args: Array[String]): Unit = {
    val response = HttpClient.get("httpbin.org", "/get")
    val body = HttpClient.readBody(response)

    val out = getStdout()
    try {
      val lines = Seq(
        s"Status: ${response.status()}",
        body.take(200),
      )
      lines.foreach { line =>
        out.blockingWriteAndFlush((line + "\n").getBytes().asInstanceOf[Array[scala.scalajs.wit.unsigned.UByte]])
          .getOrElse(throw new RuntimeException("Failed to write to stdout"))
      }
    } finally {
      out.close()
    }
  }
}
