lazy val root = (project in file(".")).enablePlugins(PlayScala)

name := "play-jax-ws"

organization := "net.koofr"

version := "0.1.0"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalacOptions ++= Seq(
  "-Xfatal-warnings", "-feature", "-Xlint",
  "-language:postfixOps"
)

libraryDependencies += ws % "test"
