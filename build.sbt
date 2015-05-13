import com.github.play2war.plugin._

name := """mockbird"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"