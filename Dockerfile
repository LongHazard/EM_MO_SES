FROM maven AS build-dependency
WORKDIR /opt/app-dev
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-oracle

WORKDIR /opt/app-dev
#RUN adduser --system --group twoup
#RUN chown -R twoup:twoup /opt/app-dev
#USER twoup
COPY --from=build-dependency opt/app-dev/release/*.jar .
COPY --from=build-dependency opt/app-dev/release/lib ./lib

#COPY ./release/*.jar .
#COPY ./release/lib ./lib

ENTRYPOINT java -jar /opt/app-dev/*.jar