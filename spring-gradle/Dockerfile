###############################################################################
# Build Operations Playground Image
###############################################################################

FROM gradle:7.2.0-jdk11-alpine
COPY ./backendsystem/build/libs/backendsystem-0.0.1-SNAPSHOT.jar /opt/app.jar
ENTRYPOINT ["java","-jar","/opt/app.jar"]