#!/bin/sh
    
    # Gradle wrapper
    if [ ! -f "gradlew" ]; then
        touch gradlew
    fi
    
    if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
        echo "Downloading Gradle..."
        mkdir -p gradle/wrapper
        
        curl -sL "https://raw.githubusercontent.com/nicklausw/gradle-wrapper/main/gradlew" -o gradlew 2>/dev/null || cat > gradlew << 'SCRIPT'
    #!/bin/bash
    GRADLE_VERSION=8.2
    APP_NAME="Gradle"
    APP_BASE_NAME=$(basename "$0")
    APP_HOME=$(cd "$(dirname "$0")" && pwd)
    
    # Determine Java command
    if [ -n "$JAVA_HOME" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        JAVACMD="java"
    fi
    
    CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
    
    exec "$JAVACMD"         $DEFAULT_JVM_OPTS         $JAVA_OPTS         $GRADLE_OPTS         "-Dorg.gradle.appname=$APP_BASE_NAME"         -classpath "$CLASSPATH"         org.gradle.wrapper.GradleWrapperMain "$@"
    SCRIPT
        chmod +x gradlew
    fi
    
    ./gradlew "$@"
    