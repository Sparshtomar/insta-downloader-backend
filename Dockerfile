# ---------- Stage 1: Build the Spring Boot app ----------
    FROM gradle:8.5.0-jdk17 as builder

    WORKDIR /app
    COPY --chown=gradle:gradle . /app
    RUN gradle build --no-daemon
    
    # ---------- Stage 2: Setup runtime with Python + Playwright ----------
    FROM python:3.10-slim
    
    # Install Java and system dependencies for Playwright
    RUN apt-get update && \
        apt-get install -y openjdk-17-jdk curl wget gnupg unzip fonts-liberation \
        libnss3 libatk1.0-0 libatk-bridge2.0-0 libx11-xcb1 libxcomposite1 libxdamage1 \
        libxrandr2 libgbm1 libasound2 libxshmfence1 libxss1 libxtst6 lsb-release && \
        pip install --no-cache-dir playwright && \
        playwright install --with-deps && \
        apt-get clean && rm -rf /var/lib/apt/lists/*
    
    # Copy built Spring Boot app JAR from builder
    COPY --from=builder /app/build/libs/*.jar /app/app.jar
    
    # Copy your scripts if needed
    COPY scripts/ /scripts/
    
    # Set working directory
    WORKDIR /app
    
    EXPOSE 8080
    
    # Start Spring Boot app
    CMD ["java", "-jar", "/app/app.jar"]
    