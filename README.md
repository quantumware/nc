# README

## Build
sbt assembly

## Employee Associates
java -cp target/scala-2.11/nc-assembly-0.0.1.jar labs.nc.Main

## URL Pinger
- To download
java -cp target/scala-2.11/nc-assembly-0.0.1.jar labs.nc.UrlPinger http://www.yahoo.com http://www.google.com

- To search
 java -cp target/scala-2.11/nc-assembly-0.0.1.jar labs.nc.UrlPinger -s header Set-Cookie