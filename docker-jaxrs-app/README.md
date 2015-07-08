## Run a WildFly-Swarm microservice in Docker

You need to have Docker and docker-compose installed. 

### Packaging your application
  
Run `mvn clean package`. A `DockerJaxRS-swarm.jar` file will be built in your target folder  
 
### Building the Docker image

You build the Docker image with the command: `docker-compose build`

This will add the `jar` file to the Docker image and build it.  

### Running Docker with a Wildfly-Swarm microservice inside

You run the Docker container and start the Wildfly-Swarm microservice (a simple JAX-RS application) with the following commands:

`docker-compose up` 

OR

`docker run -p 8080:8080 dockerjaxrsapp_wildflyswarm`

### Inspect your running Docker containers

From a terminal run `docker ps` and you should see something like: 

````
CONTAINER ID        IMAGE                                COMMAND                CREATED              STATUS              PORTS                    NAMES
c618ed118938        dockerjaxrsapp_wildflyswarm:latest   "java -jar /opt/Dock   About a minute ago   Up About a minute   0.0.0.0:8080->8080/tcp   dockerjaxrsapp_wildflyswarm_1
````

Now try to `curl` the resource running inside Docker:

````
curl localhost:8080/resource
````

Result should be the following:

````
bar
````








