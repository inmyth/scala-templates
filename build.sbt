name := "scala-templates"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "io.monix" %% "monix" % "3.0.0"
libraryDependencies += "io.monix" %% "monix-reactive" % "3.1.0"
libraryDependencies += "com.typesafe" % "config" % "1.4.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"


// MySql/Postgres
libraryDependencies += "com.github.jasync-sql" % "jasync-mysql" % "1.0.12"

//Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-M2" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % Test
// Not important, use these for mocking
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies += "com.datastax.oss" % "java-driver-core" % "4.3.1"

