#!/bin/sh

javac -cp "lib/*" -d out $(find src/main/java -name "*.java")

java -cp "out:lib/*" com.blocksim.presentation.console.MainApp