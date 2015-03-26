name := """example-scala"""

version := "1.0-SNAPSHOT"

lazy val playJaxWs = file("..")

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(playJaxWs)

scalaVersion := "2.10.4"
