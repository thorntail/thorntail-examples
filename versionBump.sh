#!/bin/sh

# Use this script to bump the version accross all POMs.

PROGNAME=`basename "$0"`

if [ "$#" -ne 1 ]; then
    echo "Illegal number of arguments. Use '$PROGNAME <version>'"
else
    mvn versions:set -DnewVersion=$1
    find . -name pom.xml.versionsBackup -exec rm {} \;
fi
