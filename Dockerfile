FROM openjdk:8

ADD target/employee-portal-boot.jar employee-portal-boot.jar

ENTRYPOINT ["java", "-jar", "/employee-portal-boot.jar"] 

EXPOSE 8080