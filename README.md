# IoT Sense

## Overview

This is IoT Sense, the gateway to all home smart devices. IoT sense is a realtime gateway that connects your smart devices
with your smartphone. 

### Key Features

The project is built around the idea of non-blocking operations and realtime functionalities.

* Data Ingestion via Kafka: The application exchanges data via kafka to ensure high throughput and scalability in data transmission.
* Real-Time Data Streaming: The application streams data to connected mobile applications using real-time communication 
* protocols, like websocket and firebase notifications.
* Scalable Design: Built to handle high volumes of data and users, the application can easily scale to meet the growing needs of IoT environments.
* Module oriented: Each module is separated and decoupled from the core, in this way you can extend or activate only modules that you need.

## Architecture

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

1. Kafka Integration: This application subscribes to Kafka topics and consumes messages containing IoT data from various sources (sensors, devices, etc.).
2. PostgreSQL database.
3. Mobile Application Streaming: Using WebSockets and Redis for decouple data ingestion and WebSocket updates.

## Getting Started

### Overview

The application relies on some settings to run correctly and these settings are about context where the application is going to run. To allow users to customize the application, there a few settings that are needed to be set.
The application needs to be configured via following environment variables:
* `DB_HOST`: MongoDB host name
* `DB_USER`: MongoDB user credentials to login
* `DB_PASSWD`: MongoDB password to login
* `DB_NAME`: The name where the you whant store the data
* `KAFKA_BOOTSTRAP_SERVERS`: The Kafka brokers base url
* `HUB_SLUG`: The name of setted Hub in platform configuration phase (see the CLI utilities [🚧 Work in progress] to learn more)
* `PUBLIC_KEY_LOCATION`: The pem public file to use to validate the JWT (see the CLI utility to know how to generate it)
* `JWT_AUDIENCE`: The audience of the JWT (same used to generate the JWT with te CLI utility)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/iot-sense-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.
