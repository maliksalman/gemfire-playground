# gemfire-server

This is a spring-boot app which can act as a gemfire server. This server is appropriate for development and testing gemfire capabilities. Running this app will expose a single node gemfire cache-server and a locator-server. It includes two cache regions:

1. A `generic-cache` region which defines `java.lang.String` as both the key and value.
2. A `session-cache` region where the keys are `java.lang.Object` and values are `org.springframework.session.data.gemfire.AbstractGemFireOperationsSessionRepository.GemFireSession`. This is the region definition that spring assumes when you use the `@EnableGemFireHttpSession` in your application.

## Compiling

This is a apache maven app so `mvn clean package` will create a jar file in the `target` directory.

## Running

The following command will start the application with the default configuration values (the most important is the locator port, which is how gemfire clients connect - it is set to 10334 by default):

```
mvn spring-boot:run
```

The following properties can be overridden:

| Property | Default Value |
| :---- | :---: |
| `gemfire.log.level` | `config` |
| `gemfire.locator.host-port` | `localhost[10334]` |
| `gemfire.manager.port` | `1099` |
| `gemfire.cache.server.bind-address` | `localhost` |
| `gemfire.cache.server.hostname-for-clients` | `localhost` |
| `gemfire.cache.server.port` | `40404` |

For example, to change the locator port to 23456, run the following:

```
mvn spring-boot:run '-Dgemfire.locator.host-port=localhost[23456]'
```
