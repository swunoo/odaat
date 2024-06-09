FROM openjdk:11 AS builder

RUN \
  curl -Lo sbt-1.5.5.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-1.5.5.deb && \
  dpkg -i sbt-1.5.5.deb && \
  rm sbt-1.5.5.deb

WORKDIR /app

COPY . .

RUN sbt stage

FROM openjdk:11

WORKDIR /app

COPY --from=builder /app/target/universal/stage .

EXPOSE 9000

CMD ["./bin/odaat-server"]
