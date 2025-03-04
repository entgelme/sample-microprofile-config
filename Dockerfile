FROM icr.io/appcafe/open-liberty:kernel-slim-java11-openj9-ubi

ARG VERSION=1.0
ARG REVISION=SNAPSHOT

LABEL \
  org.opencontainers.image.authors="Your Name" \
  org.opencontainers.image.vendor="IBM" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.source="https://github.com/OpenLiberty/guide-getting-started" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  vendor="Open Liberty" \
  name="system" \
  version="$VERSION-$REVISION" \
  summary="Demonstrates an API to return the Microprofile configuration sources and their settings." \
  description="Also demonstrates the addition of an external JSON config source, e.g. from a ConfigMap. Uses the mpconfig-3.1 feature. \
    Derived from the OpenLiberty Getting Started guide"

COPY --chown=1001:0 src/main/liberty/config/ /config/

RUN features.sh
COPY --chown=1001:0 target/*.war /config/apps/
# Create a mount point to place the CustomConfigSource.json there by mounting it from a ConfigMap
USER root
RUN mkdir /wlpCustomConfig && chown 1001:0 /wlpCustomConfig
USER default
# COPY --chown=1001:0 resources/CustomConfigSource.json /wlpCustomConfig/
RUN configure.sh  
