#!/bin/sh

mkdir -p out

javac -cp "out:lib/*" -d out $(find src/test/java -name "*.java")

java -jar lib/junit-platform-console-standalone-1.9.3.jar \
    --class-path out \
    --scan-class-path