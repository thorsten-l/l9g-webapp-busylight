# l9g-webapp-busylight

## Build

`mvn clean install`

## Run
The JAR file is executable.

### Linux
`sudo ./target/busylight.jar`

### macOS
`./target/busylight.jar`

### Windows 
`.\target\busylight.jar` (starts in background)

`java -jar .\target\busylight.jar` (starts in forground and shows logging)

## Busylight API Documentation
- http://localhost:31415

## Requirements

### Hardware
- https://luxafor.de/products/busylight

### Software
- Java JDK 21 - https://bell-sw.com/pages/downloads/
- Maven - https://maven.apache.org/
- HID4Java - https://github.com/gary-rowe/hid4java

# Successfully tested
- MacBook Pro, M3 Max, macOS 14.7.2
- Dell Inspiron, i5, Ubuntu Linux 22.04.5 LTS
- Dell Inspiron, i7, Windows 11
