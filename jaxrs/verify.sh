#!/usr/bin/env bash

START_TIMEOUT=30
JAR=target/*-swarm.jar
PID=nothing

function verify() {
  httpShould http://localhost:8080 'Howdy at'
  return $?
}

function start() {
  rm -f verify.log
  java -jar $JAR 2>&1 > verify.log &
  PID=$!
  sleep $START_TIMEOUT
}

function stop() {
  kill -9 $PID
}

function httpShould() {
  url=$1
  content=$2
  curl -s $url | grep -q "$content" || ( echo "HTTP did not respond wtih $content" && false )
  return $?
}

function logShould() {
  content=$1
  grep -q $1 verify.log || ( echo "Log did not contain $1" && false )
  return $?
}

start
verify
RESULT=$?
stop

if [ $RESULT -eq 0 ]; then
  echo "SUCCESS"
else
  echo "FAILURE"
fi

exit $RESULT
