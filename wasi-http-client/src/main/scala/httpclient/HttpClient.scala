package httpclient

import httpclient.wasi.http.outgoing_handler
import httpclient.wasi.http.types._
import httpclient.wasi.io.streams.StreamError
import scala.scalajs.wit

object HttpClient {
  def get(authority: String, path: String): IncomingResponse = {
    val request = OutgoingRequest(Fields())

    val future = (for {
      _ <- request.setMethod(Method.Get)
      _ <- request.setScheme(wit.Some(Scheme.Https))
      _ <- request.setAuthority(wit.Some(authority))
      _ <- request.setPathWithQuery(wit.Some(path))
      future <- outgoing_handler.handle(request, wit.None)
    } yield future)
      .mapErr(err => throw new RuntimeException(s"request failed: $err"))
      .get

    val pollable = future.subscribe()
    try pollable.block()
    finally pollable.close()

    (for {
      outer <- future.get()
    } yield {
      (for {
        inner <- outer
        response <- inner
      } yield response)
        .mapErr(err => throw new RuntimeException(s"response failed: $err"))
        .get
    }).getOrElse(throw new RuntimeException("response is not ready"))
  }

  def readBody(response: IncomingResponse): String = {
    val stream = (for {
      body <- response.consume()
      stream <- body.stream()
    } yield stream).getOrElse(throw new RuntimeException("failed to read response body"))
    val bytes = scala.collection.mutable.ArrayBuffer.empty[Byte]
    var done = false

    while (!done) {
      stream.blockingRead(65536L) match {
        case wit.Ok(chunk) =>
          bytes ++= chunk.map(_.toByte)
        case wit.Err(StreamError.Closed) =>
          done = true
        case wit.Err(err) =>
          throw new RuntimeException(s"failed to read response body: $err")
      }
    }

    new String(bytes.toArray, "UTF-8")
  }
}
