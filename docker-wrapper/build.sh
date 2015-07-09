#!/usr/bin/env bash

# Copy jar file to this directory
cp $1 wildfly-swarm.jar

# Build Docker image
docker-compose build
