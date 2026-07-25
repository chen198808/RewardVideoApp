#!/bin/sh
    
    # Gradle wrapper - 下载真正的gradle wrapper并委托给它
    
    # Determine the project base dir
    PRG="$0"
    PRGDIR=$(dirname "$PRG")
    
    # Download gradle wrapper if needed
    if [ ! -f "$PRGDIR/gradle/wrapper/gradle-wrapper.jar" ]; then
        echo "Downloading Gradle..."
        mkdir -p "$PRGDIR/gradle/wrapper"
        # Download gradle-wrapper.jar from Maven
        curl -sL "https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar" -o "$PRGDIR/gradle/wrapper/gradle-wrapper.jar" 2>/dev/null ||         curl -sL "https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar" -o "$PRGDIR/gradle/wrapper/gradle-wrapper.jar"
    fi
    
    # Also ensure gradle-wrapper.properties exists
    if [ ! -f "$PRGDIR/gradle/wrapper/gradle-wrapper.properties" ]; then
        mkdir -p "$PRGDIR/gradle/wrapper"
        cat > "$PRGDIR/gradle/wrapper/gradle-wrapper.properties" << 'WRAPPERPROPS'
    distributionBase=GRADLE_USER_HOME
    distributionPath=wrapper/dists
    distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
    networkTimeout=10000
    validateDistributionUrl=true
    zipStoreBase=GRADLE_USER_HOME
    zipStorePath=wrapper/dists
    WRAPPERPROPS
    fi
    
    # Execute the wrapper
    exec "$JAVA_HOME/bin/java" -jar "$PRGDIR/gradle/wrapper/gradle-wrapper.jar" "$@"
    