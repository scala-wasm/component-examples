package rustcompose

import rustcompose.scala_wasm.rust_compose.greeter.greet

object RunImpl {
  def main(args: Array[String]): Unit =
    Console.println(greet("Scala"))
}
