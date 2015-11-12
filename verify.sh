#!/usr/bin/env bash

# Verify JAX-RS example
cd ./jaxrs/
./verify.sh
cd ..

# Verify Messaging example
cd ./messaging/
./verify.sh
cd ..


# Verify Static example
cd ./static/
./verify.sh
cd ..
