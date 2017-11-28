# Gemfire Playground
A bunch of spring-boot apps to demonstrate how to use gemfire for object or http-session caching.

## [gemfire-server](./gemfire-server/)
This is a boot app which starts a single node gemfire cache-server and locator-server.

## [gemfire-client-region](./gemfire-client-region/)
This is a boot app which acts as a gemfire client to cache simple string objects.

## [gemfire-client-session](./gemfire-client-session/)
This is a boot app which uses the spring-session abstraction to cache http-session objects in gemfire. The gemfire server needs to have access to the same version of spring-session classes as used in the client.

## [gemfire-client-json-session](./gemfire-client-json-session/)
This is a boot app which uses the spring-session abstraction to cache http-session objects in gemfire. It implements a custom spring-session repository which serializes/deserializes http-session object to/from JSON strings. This means that the gemfire server doesn't need to be aware of the java classes used in the client.
