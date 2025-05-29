# Sử dụng image OpenJDK làm base image
FROM eclipse-temurin:17-jdk-jammy

# Thư mục chứa app trong container
WORKDIR /app

# Copy file jar vào container
COPY target/flyora-backend.jar app.jar

# Expose cổng ứng dụng (8080 theo cấu hình trong application.properties)
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]


