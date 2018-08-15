#!/usr/bin/env bash

# Copy jar file to this directory
cp $1 thorntail.jar

# Build Docker image
docker-compose build
