name := """example-java"""

version := "1.0-SNAPSHOT"

lazy val playJaxWs = file("..")

lazy val root = (project in file(".")).enablePlugins(PlayJava).dependsOn(playJaxWs)

scalaVersion := "2.10.4"
