###############################################################################
# Build Operations Playground Image
###############################################################################

FROM flink:scala_2.12-java11

WORKDIR /opt/flink-maven
COPY ./flink/run.sh /run.sh
COPY ./flink/target/dependency/* /opt/flink/lib/
COPY ./flink/target/flink-*.jar /opt/job.jar
