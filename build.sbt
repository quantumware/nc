//import AssemblyKeys._

//assemblySettings

name := "nc"

version := "0.0.1"

scalaVersion := "2.11.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  "org.xerial" % "sqlite-jdbc" % "3.7.2",
  "org.mongodb" % "mongo-java-driver" % "3.2.1",
  "com.h2database" % "h2" % "1.4.193",
  "org.eclipse.persistence" % "eclipselink" % "2.5.0",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.7",
  "com.google.code.gson" % "gson" % "2.8.0",
  "org.apache.commons" % "commons-lang3" % "3.0",
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

//mainClass in (Compile, run) := Some("labs.jy4.MongoDB")
mainClass in (Compile, run) := Some("labs.nc.UrlPinger")
//mainClass in assembly := Some("labs.nc.Main")
//test in assembly := {}
//assemblyJarName in assembly := { s"${name.value}-${version.value}.jar" }

resolvers ++= Seq(
  "Central Repository" at "https://repo1.maven.org/maven2/",
  "JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/",
  "Spray Repository" at "http://repo.spray.cc/",
  //"Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "Akka Repository" at "http://repo.akka.io/releases/",
  //"Twitter4J Repository" at "http://twitter4j.org/maven2/",
  "Apache HBase" at "https://repository.apache.org/content/repositories/releases",
  //"Twitter Maven Repo" at "http://maven.twttr.com/",
  "scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Mesosphere Public Repository" at "http://downloads.mesosphere.io/maven",
  Resolver.sonatypeRepo("public")
)

assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x => MergeStrategy.last
    //val oldStrategy = (assemblyMergeStrategy in assembly).value
    //oldStrategy(x)
}

/*mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
    //case "META-INFO/services/org.apache.hadoop.fs.FileSystem" => MergeStrategy.concat
    //case m if m.startsWith("META-INF") => MergeStrategy.discard
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache", xs @ _*) => MergeStrategy.first
    case PathList("org", "jboss", xs @ _*) => MergeStrategy.first
    case "about.html"  => MergeStrategy.rename
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
}*/
