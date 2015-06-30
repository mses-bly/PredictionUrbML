import AssemblyKeys._

assemblySettings

name := "PredictionUrbML"

organization := "urb.ml"

libraryDependencies ++= Seq(
  "io.prediction" %% "core" % pioVersion.value % "provided",
  "org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.3.0" % "provided",
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
  "com.google.guava" % "guava" % "12.0"
)