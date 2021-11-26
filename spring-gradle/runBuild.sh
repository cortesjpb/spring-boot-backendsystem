cd spring-gradle/backendsystem/;
gradle build;
cd ..;
docker image rm spring_backend;
docker build . -t spring_backend;
cd ..;