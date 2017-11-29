# gemfire-client-session

This is a spring-boot app which demonstrates how to store HTTP sessions via spring-session in gemfire. Once configured, the developer interacts directly with `javax.servlet.HttpServlet` objects and is unaware of spring-session or gemfire involvement. The app assumes that a gemfire cahce-server and a locator is available and has defined a cache region named `session-cache` where the key/value are defined as `java.lang.Object`/`org.springframework.session.data.gemfire.AbstractGemFireOperationsSessionRepository.GemFireSession`. 

The region requirements are dictated by `@EnableGemFireHttpSession` used in this app. The requirement that both the client app and the gemfire server share the exact same classes might not be desirable. For a possible solution we can try to serialize the session objects to a neutral format (like JSON) before storing them in gemfire - the  [gemfire-client-json-session](../gemfire-client-json-session/) app shows how to do that.

This is a web app and exposes `/hello` endpoint. When visiting that endpoint in a browser, a hello message with a visit counter is shown. The visit counter will increment on every visit with the counter being persisted in the `javax.servlet.http.HttpSession` object (backed by spring-session and gemfire). If testing with `curl` issue the following command:

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

For example, to change the locator port to 23456, run the following:

```
mvn spring-boot:run '-Dgemfire.locator.port=23456'
```
