name := "scala-templates"

version := "0.1"

scalaVersion := "2.13.1"


resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

libraryDependencies += "io.monix" %% "monix" % "3.0.0"
libraryDependencies += "io.monix" %% "monix-reactive" % "3.1.0"
libraryDependencies += "com.typesafe" % "config" % "1.4.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"

// CSV
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"

// MySql/Postgres
libraryDependencies += "com.github.jasync-sql" % "jasync-mysql" % "1.0.12"

// Cassandra
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.8.0"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.8.0"
libraryDependencies += "com.datastax.oss" % "java-driver-core" % "4.3.1"

//Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-M2" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % Test

