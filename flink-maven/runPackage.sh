cd flink-maven/flink/;
mvn package;
cd ..;
docker image rm flink_kafka;
docker build . -t flink_kafka;
cd ..;