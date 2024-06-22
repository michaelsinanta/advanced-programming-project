<div align="center">
    <h1>Bayar Service</h1>
</div>

## API Documentation

- [API Docs]()

## Low level Tech Doc

- [Melihat ringkasan tagihan usecase]()
- [TBC]()
- [TBC]()

## Setup local development

### Install tools

- [IntelliJ Ultimate](https://www.jetbrains.com/idea/download/#section=linux)
- [Docker](https://docs.docker.com/get-docker/)

First define environmental variables in `.env` in root directory. 
Copy the env variables in .env.example file and fill the appropriate value.

### With Docker
Make sure Docker Engine is running. Run commands below to archive jar file.

```
./gradlew bootJar
```

`build` folder should be created in root directory. 
While in **root directory**, build docker images and run them with docker-compose. This might take up to few minutes.

```shell script
docker-compose up
```

Application should be up and running: app `127.0.0.1:8000`, postgres `127.0.0.1:5432`.

Bringing down containers with **optional** -v flag removes **all** attached volumes and invalidates caches.

```shell script
docker-compose down
```

To run commands in active container:

```shell script
docker exec -it <CONTAINER_ID/CONTAINER_NAME> <command>
```

e.g

```shell script
docker exec -it bayar-service-db createdb --username=admin --owner=admin bayar-db (create database)
docker exec -it bayar-service-db psql -U admin -d bayar-service (run postgres terminal inside the container )
```

## Production Deployment
- TBC
