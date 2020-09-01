FROM openjdk:8
EXPOSE 8080
ADD target/employee-portal-boot.jar employee-portal-boot.jar
ENTRYPOINT ["java", "-jar", "/employee-portal-boot.jar"] 