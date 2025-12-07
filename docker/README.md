## Build image

```bash
docker build \
  --build-arg JWIZARD_VERSION=latest \
  --build-arg JWIZARD_MAVEN_NAME=<maven m2 repository username> \
  --build-arg JWIZARD_MAVEN_SECRET=<maven m2 repository secret> \
  -t milosz08/jwizard-api .
```

## Create container

* Using command:

```bash
docker run -d \
  --name jwizard-api \
  -p 8080:8080 \
  -e JWIZARD_VAULT_SERVER=<vault server url> \
  -e JWIZARD_VAULT_USERNAME=<vault username> \
  -e JWIZARD_VAULT_PASSWORD=<vault password> \
  -e JWIZARD_XMS=1024m \
  -e JWIZARD_XMX=1024m \
  milosz08/jwizard-api:latest
```

* Using `docker-compose.yml` file:

```yaml
services:
  jwizard-api:
    container_name: jwizard-api
    image: milosz08/jwizard-api:latest
    ports:
      - '8080:8080'
    environment:
      JWIZARD_VAULT_SERVER: <vault server url>
      JWIZARD_VAULT_USERNAME: <vault username>
      JWIZARD_VAULT_PASSWORD: <vault password>
      JWIZARD_XMS: 1024m
      JWIZARD_XMX: 1024m
    networks:
      - jwizard-network

  # other containers...

networks:
  jwizard-network:
    driver: bridge
```

## License

This project is licensed under the Apache 2.0 License.
