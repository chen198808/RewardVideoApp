#!/bin/sh

# Add Gradle wrapper if not present
if [ ! -f "gradlew" ]; then
    touch gradlew
fi

# Download gradle wrapper
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    mkdir -p gradle/wrapper
    # Use gradle wrapper
    curl -sL "https://services.gradle.org/distributions/gradle-8.2-bin.zip" -o /tmp/gradle.zip
    unzip -q /tmp/gradle.zip -d /tmp/gradle
    /tmp/gradle/gradle-8.2/bin/gradle wrapper --gradle-version 8.2
fi

./gradlew "$@"
