![](.github/banner.png)

JWizard is an open-source Discord music bot handling audio content from various multimedia sources with innovative web
player. This repository contains an API used by the web client to manage guilds, playlists, and remotely invoke Discord
commands using its own protocol based on WebSocket and RabbitMQ message queue.

## Table of content

* [Architecture concepts](#architecture-concepts)
* [Project modules](#project-modules)
* [Clone and install](#clone-and-install)
* [Documentation](#documentation)
* [Contributing](#contributing)
* [License](#license)

## Architecture concepts

* This project is based on a REST API, utilizing a lightweight built-in Jetty server and the Javalin library.
* Spring Boot was omitted in favor of implementing only the IoC container provided by Spring Context.
* Configuration is shared with the JWizard Lib project to eliminate code redundancy and ensure consistency in changes
  across different project modules.
* This project is divided into several loosely coupled modules connected through the SPI pattern and auto-injected
  beans by IoC container.

## Project modules

TBD

## Clone and install

1. Make sure you have at least JDK 17 and Kotlin 2.0.
2. Clone **JWizard Lib** and **JWizard Tools** from organization repository via:

```bash
$ git clone https://github.com/jwizard-bot/jwizard-lib
$ git clone https://github.com/jwizard-bot/jwizard-tools
```

3. Configure and run all necessary containers defined in `README.md` file in this repository. You must have up these
   containers:

| Name             | Port(s) | Description                  |
|------------------|---------|------------------------------|
| jwizard-vault    | 8761    | Secret keys storage service. |
| jwizard-mysql-db | 8762    | MySQL database.              |

> NOTE: Don't forget to perform database migration after start DB (see `jwizard-lib` repository).

4. Build library and package to Maven Local artifacts' storage:

* for UNIX based systems:

```bash
$ ./gradlew clean publishToMavenLocal
```

* for Windows systems:

```bash
.\gradlew clean publishToMavenLocal
```

5. Clone this repository via:

```bash
$ git clone https://github.com/jwizard-bot/jwizard-api
```

6. Create `.env` file in root of the project path (based on `example.env`) and insert Vault token:

```properties
ENV_VAULT_TOKEN=<vault token>
```

where `<value token>` property is the Vault token stored in configured `.env` file in `jwizard-infa` repository.

5. That's it. Now you can run via Intellij IDEA. Make sure, you have set JVM parameters:

```
-Druntime.profiles=dev -Denv.enabled=true -Xms128m -Xmx128m
```

where `Xmx` and `Xms` parameters are optional and can be modified.

> NOTE: For servers running on HotSpot JVM, Oracle recommended same Xms and Xmx parameter, ex. `-Xms128m` and
`-Xmx128m`. More information you will
> find [here](https://docs.oracle.com/cd/E74363_01/ohi_vbp_-_installation_guide--20160224-094432-html-chunked/s66.html).

## Documentation

For detailed documentation, please visit [JWizard documentation](https://jwizard.pl/docs).
<br>
Documentation for latest version (with SHA) you will find [here](https://docs.jwizard.pl/jwa) - in KDoc format.

## Contributing

We welcome contributions from the community! Please read our [CONTRIBUTING](./CONTRIBUTING) file for guidelines on how
to get involved.

## License

This project is licensed under the AGPL-3.0 License - see the LICENSE file for details.
