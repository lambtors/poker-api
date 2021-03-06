/** ********* PROJECT INFO ******************/
name := "poker_api"
version := "0.0.1"

/** ********* PROJECT SETTINGS ******************/
Configuration.settings

/** ********* PROD DEPENDENCIES *****************/
resolvers += "Habla repo - releases" at "http://repo.hablapps.com/releases"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time"   % "2.16.0",
  "com.lihaoyi"            %% "pprint"        % "0.5.2",
  "com.typesafe.akka"      %% "akka-http"     % "10.0.9",
  "ca.mrvisser"            %% "sealerate"     % "0.0.5",
  "org.typelevel"          %% "cats-core"     % "1.0.0-MF",
  "org.typelevel"          %% "cats-mtl-core" % "0.0.2"
)

/** ********* TEST DEPENDENCIES *****************/
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"     % "3.0.1" % Test,
  "org.hablapps"  %% "puretest-cats" % "0.2"   % Test
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
