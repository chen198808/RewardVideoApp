#!/bin/sh
    
    # Gradle wrapper for RewardVideoApp
    SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
    JAR="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar"
    
    # Run gradle
    java -jar "$JAR" "$@"
    