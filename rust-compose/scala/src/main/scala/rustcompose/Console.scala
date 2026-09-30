package rustcompose

import io.github.scalawasm.wasi4s.wasi.cli.stdout._
import scala.scalajs.wit.unsigned.UByte

object Console {
  def println(s: String): Unit = {
    val out = getStdout()
    try {
      val bytes = (s + "\n").getBytes().asInstanceOf[Array[UByte]]
      out.blockingWriteAndFlush(bytes).getOrElse(
        throw new RuntimeException("Failed to write to stdout"))
    } finally {
      out.close()
    }
  }
}
