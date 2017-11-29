# gemfire-client-json-session

This is a spring-boot app which demonstrates how to store HTTP sessions via spring-session in gemfire. Instead of using the implementation provided by `@EnableGemFireHttpSession`, this app implements a custom spring-session repository. This repository will convert all objects in the HTTP session to/from JSON before interacting with gemfire. As usual with spring-session, the developer interacts directly with `javax.servlet.HttpServlet` objects and is unaware of spring-session or gemfire involvement.

The app assumes that a gemfire cahce-server and a locator is available and has defined a cache region where the key/value are both defined as `java.lang.String`. The advantage with this apporoach is that the gemfire server doesn't need to be aware of the session implemention classes as is the case with the [gemfire-client-session](../gemfire-client-session/).

This is a web app and exposes `/hello` endpoint. When visiting that endpoint in a browser, a hello message with a visit counter is shown. The visit counter will increment on every visit with the counter being persisted in the `javax.servlet.http.HttpSession` object (backed by spring-session with our custom gemfire repository). If testing with `curl` issue the following command:

```
curl -c cookiejar.txt -b cookiejar.txt http://localhost:8080/hello
```

Notice the usage of a `cookiejar.txt` file, this is needed to simulate how a browser acts when cookies are involved. In this case the session IDs are communicated via cookies.

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
