# Keycloak Example

Simple JAX-RS application with Keycloak example.

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/THORN

This example shows how the command-line and browser clients can access a JAX-RS service
secured by Keycloak.

## Start Keycloak

Here, we offer 3 options how to start Keycloak.
Select one of them and proceed according to the instructions.

### Keycloak Thorntail standalone server

Download the latest Thorntail Keycloak standalone server JAR, for example, 2.4.0.Final version:

```sh
wget http://repo1.maven.org/maven2/io/thorntail/servers/keycloak/2.4.0.Final/keycloak-2.4.0.Final-thorntail.jar
```

and start it:

```sh
THIS_EXAMPLE=/path/to/this/example
java -Dthorntail.http.port=8180 \
     -Dkeycloak.migration.action=import \
     -Dkeycloak.migration.provider=dir \
     -Dkeycloak.migration.dir=${THIS_EXAMPLE}/realm \
     -jar keycloak-2.4.0.Final-thorntail.jar
```

### Local Keycloak installation

This assumes you already have Keycloak installed in `$KEYCLOAK_HOME`.

```sh
THIS_EXAMPLE=/path/to/this/example
cd $KEYCLOAK_HOME
bin/standalone.sh \
  -Djboss.socket.binding.port-offset=100 \
  -Dkeycloak.migration.action=import \
  -Dkeycloak.migration.provider=dir \
  -Dkeycloak.migration.dir=${THIS_EXAMPLE}/realm
```

### Docker

This assumes you have the Docker daemon running.

```sh
THIS_EXAMPLE=/path/to/this/example
docker run -it -d \
  -p 8180:8080 \
  -v $THIS_EXAMPLE/realm:/tmp/realm \
  jboss/keycloak:4.8.3.Final \
  -b 0.0.0.0 \
  -Dkeycloak.migration.action=import \
  -Dkeycloak.migration.provider=dir \
  -Dkeycloak.migration.dir=/tmp/realm
```

## Build Example

```sh
mvn clean package
```

## Access a secured resource from shell

### Start the example application

```sh
java -Dthorntail.keycloak.json.path=classpath:cmd-client-keycloak.json -jar target/example-keycloak-thorntail.jar
```

### Access the secured resource

```sh
curl -v http://localhost:8080/app/secured
```

You'll get the response with `401 Unauthorized`. Let's get a Token to access it.

### Obtain token from Keycloak server

```sh
USER=user1
PASS=password1
RESULT=$(curl -s --data "grant_type=password&client_id=thorntail-cmd-client-example&username=${USER}&password=${PASS}" http://localhost:8180/auth/realms/thorntail-cmd-client/protocol/openid-connect/token)
TOKEN=$(echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g')
# alternatively, if you have jq installed:
# TOKEN=$(echo $RESULT | jq -r '.access_token')
```

### Access the secured resource with the token

```sh
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/app/secured
```

You'll get the response which contains `Hi user1, this is Secured Resource`.

## Access a secured resource from browser

### Start the example application

Stop the example application if it is already running and start it again with:

```sh
java -Dthorntail.keycloak.json.path=classpath:browser-client-keycloak.json -jar target/example-keycloak-thorntail.jar
```

Make sure you keep the Keycloak server running.

### Access the public client page

Navigate the browser to "http://localhost:8080/" 

### Access the secured resource

Click on "Access Secured Service"
You'll get `Unauthorized` displayed.

### Login

Click on "Login" and use the `user1`/`password1` credentials to login to Keycloak.

### Access the secured resource

Click on "Access Secured Service" again.

You'll get the response which contains `Hi user1, this is Secured Resource`.

