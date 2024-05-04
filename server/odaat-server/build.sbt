name := """odaat-server"""
organization := "com.odaat"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"

ThisBuild / evictionErrorLevel := Level.Info

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.odaat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.odaat.binders._"
