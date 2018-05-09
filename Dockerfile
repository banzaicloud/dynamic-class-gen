FROM maven:3.5-jdk-10-slim as builder
ADD . /dynamic-class-gen
WORKDIR /dynamic-class-gen
RUN mvn package

FROM openjdk:10-slim
WORKDIR /opt/dynamic-class-gen/lib
COPY --from=builder /dynamic-class-gen/target/dynamicclassgen-1.0.jar .
COPY --from=builder /dynamic-class-gen/target/dependency/javassist-3.22.0-GA.jar .

CMD $JAVA_HOME/bin/java $JVM_OPTS -cp javassist-3.22.0-GA.jar:dynamicclassgen-1.0.jar com.banzaicloud.dynclassgen.DynamicClassGen