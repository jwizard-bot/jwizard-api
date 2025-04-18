FROM eclipse-temurin:17-jdk-alpine AS build

SHELL ["/bin/sh", "-c"]

# install missing xargs library
RUN apk update
RUN apk add --no-cache findutils

ENV BUILD_DIR=/build/jwa

ARG JWIZARD_VERSION
# environment variables for M2 Central Repository Service
ARG JWIZARD_MAVEN_NAME
ARG JWIZARD_MAVEN_SECRET

# ignore secret warning (in multi-stage image is irrelevant)
ENV JWIZARD_MAVEN_NAME=$JWIZARD_MAVEN_NAME
ENV JWIZARD_MAVEN_SECRET=$JWIZARD_MAVEN_SECRET

RUN mkdir -p $BUILD_DIR
WORKDIR $BUILD_DIR

# copy only gradle-based resources for optimized caching
COPY gradle $BUILD_DIR/gradle
COPY gradlew $BUILD_DIR/gradlew
COPY build.gradle.kts $BUILD_DIR/build.gradle.kts
COPY settings.gradle.kts $BUILD_DIR/settings.gradle.kts

RUN chmod +x $BUILD_DIR/gradlew
RUN cd $BUILD_DIR

RUN ./gradlew dependencies --no-daemon

# copy rest of resources
COPY . $BUILD_DIR

RUN ./gradlew clean --no-daemon

RUN JWIZARD_VERSION=${JWIZARD_VERSION} \
  ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

ENV BUILD_DIR=/build/jwa
ENV ENTRY_DIR=/app/jwa
ENV JAR_NAME=jwizard-api.jar

WORKDIR $ENTRY_DIR

COPY --from=build $BUILD_DIR/.bin/$JAR_NAME $ENTRY_DIR/$JAR_NAME
COPY --from=build $BUILD_DIR/docker/entrypoint $ENTRY_DIR/entrypoint

RUN sed -i \
  -e "s/\$JAR_NAME/$JAR_NAME/g" \
  entrypoint

RUN chmod +x entrypoint

LABEL maintainer="JWizard <info@jwizard.pl>"

EXPOSE 8080
ENTRYPOINT [ "./entrypoint" ]
