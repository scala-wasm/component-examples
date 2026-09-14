import org.scalajs.ir.WitScope
import org.scalajs.jsenv.wasmtime.WasmtimeEnv
import org.scalajs.linker.interface.ESVersion
import org.scalajs.linker.interface.WasmComponentModuleInitializerExport
import org.scalajs.linker.interface.WasmComponentModuleInitializerExport._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "io.github.scala-wasm"
ThisBuild / scalaVersion := "2.13.18"
ThisBuild / resolvers += "Sonatype Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots/"

lazy val componentSettings = Seq(
  jsEnv := Def.uncached {
    new WasmtimeEnv(
      WasmtimeEnv.Config()
        .withArgs(List(
          "run",
          "-W", "gc,function-references,exceptions",
          "-S", "cli",
          "-S", "inherit-env",
          "-S", "inherit-network",
          "-S", "tcp",
          "-S", "http"))
        .withEnv(envVars.value)
      )
  },
  scalaJSWitDirectory := baseDirectory.value / "wit",
  Compile / scalaJSLinkerConfig := {
    val witDir = scalaJSWitDirectory.value
    val witWorld = scalaJSWitWorld.value
    (Compile / scalaJSLinkerConfig).value
      .withPrettyPrint(true)
      .withESFeatures { features =>
        features
          .withUseWebAssembly(true)
          .withESVersion(ESVersion.ES2022)
      }
      .withModuleKind(ModuleKind.WasmComponent)
      .withWasmFeatures { features =>
        features // in future, plugin should automatically set these settings
          .withWitDirectory(Some(witDir.getAbsolutePath))
          .withWitWorld(witWorld)
      }
  },
)

lazy val helloworld = project
  .in(file("helloworld"))
  .enablePlugins(ScalaJSPlugin, ScalaJSJUnitPlugin)
  .settings(componentSettings)
  .settings(
    name := "helloworld",
    scalaJSWitWorld := Some("command"),
    scalaJSWitPackage := Some("example"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withWasmFeatures(
        _.withModuleInitializerExport(Some(
          WasmComponentModuleInitializerExport(
            scope = WitScope.Interface("wasi", "cli", "run", Some("0.2.0")),
            functionName = "run",
            resultType = ResultType.ResultUnitUnit,
          ))))
    },
  )

lazy val spinTodo = project
  .in(file("spin-todo"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "spin-todo",
    moduleName := "spin-todo",
    libraryDependencies += "org.typelevel" %% "jawn-ast" % "1.7.0",
    scalaJSWitWorld := Some("todo"),
    scalaJSWitPackage := Some("spintodo")
  )

lazy val wasiHttpClient = project
  .in(file("wasi-http-client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "wasi-http-client",
    scalaJSWitWorld := Some("client"),
    scalaJSWitPackage := Some("httpclient"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withWasmFeatures(
        _.withModuleInitializerExport(Some(
          WasmComponentModuleInitializerExport(
            scope = WitScope.Interface("wasi", "cli", "run", Some("0.2.0")),
            functionName = "run",
            resultType = ResultType.ResultUnitUnit,
          ))))
    },
  )

lazy val rustComposeScala = project
  .in(file("rust-compose/scala"))
  .enablePlugins(ScalaJSPlugin)
  .settings(componentSettings)
  .settings(
    name := "rust-compose-scala",
    moduleName := "rust-compose-scala",
    scalaJSWitDirectory := baseDirectory.value / "../wit",
    scalaJSWitWorld := Some("scala"),
    scalaJSWitPackage := Some("rustcompose"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withWasmFeatures(
        _.withModuleInitializerExport(Some(
          WasmComponentModuleInitializerExport(
            scope = WitScope.Interface("wasi", "cli", "run", Some("0.2.0")),
            functionName = "run",
            resultType = ResultType.ResultUnitUnit,
          ))))
    },
  )
