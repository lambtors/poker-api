/** ********* PROJECT INFO ******************/
name := "poker_api"
version := "0.0.1"

/** ********* PROJECT SETTINGS ******************/
Configuration.settings

/** ********* PROD DEPENDENCIES *****************/
libraryDependencies ++= {
  val akkaHttpVersion = "10.0.9"
  Seq(
    "com.github.nscala-time" %% "nscala-time"          % "2.16.0",
    "com.lihaoyi"            %% "pprint"               % "0.5.2",
    "com.typesafe.akka"      %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka"      %% "akka-http-spray-json" % akkaHttpVersion,
    "io.spray"               %% "spray-json"           % "1.3.3"
  )
}

/** ********* TEST DEPENDENCIES *****************/
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"                   % "3.0.1" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)

/** ********* COMMANDS ALIASES ******************/
addCommandAlias("t", "test")
addCommandAlias("to", "testOnly")
addCommandAlias("tq", "testQuick")
addCommandAlias("tsf", "testShowFailed")

addCommandAlias("c", "compile")
addCommandAlias("tc", "test:compile")

addCommandAlias("f", "scalafmt")      // Format all files according to ScalaFmt
addCommandAlias("ft", "scalafmtTest") // Test if all files are formatted according to ScalaFmt

addCommandAlias("prep", ";c;tc;ft") // All the needed tasks before running the test
