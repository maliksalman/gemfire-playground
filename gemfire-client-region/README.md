# gemfire-client-region

This is a spring-boot app which demonstrates how gemfire can be used to cache string objects. It doesn't use any abstraction like spring-cache, instead uses the gemfire objects directly. It assumes that a gemfire cahce-server and a locator is available and has defined a cache region where both the key/value are `java.lang.String`.

This app implements `CommandLineRunner` so it will do its work on startup and then terminate. The app checks to see if a value exists in gemfire under the `magic-key` key. If a value exists under that key, it displays the value in the log output, indicates that the value was **FOUND**, and exits. If a value doesn't exist under that key, it adds a key/value to gemfire, indicates in the log output that the value was **ADDED**, and exits.

## Compiling

This is a apache maven app so `mvn clean package` will create a jar file in the `target` directory.

## Running

The following command will start the application with the default configuration values (the most important are the locator properties for the gemfire server - they are set to `localhost` and `10334` by default):

```
mvn spring-boot:run
```

The following properties can be overridden:

| Property | Default Value |
| :---- | :---: |
| `gemfire.locator.host` | `localhost` |
| `gemfire.locator.port` | `10334` |
| `gemfire.region.name` | `generic-cache` |

For example, to change the locator port to 23456, run the following:

```
mvn spring-boot:run '-Dgemfire.locator.port=23456'
```
