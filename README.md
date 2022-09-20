# fas-api

Java: 11 LTS  
STS: 4.15.1 LTS  
Spring Boot: 2.7.2 LTS  

```sh
mvnw spring-boot:run
```
 
## Docker image

```sh
docker run --name fas-mysql -p 3306:3306 --network fas-network -e MYSQL_ROOT_PASSWORD={password} -d mysql:8.0.29
```
Constrói a imagem e instancia o MySQL  


```sh
mvnw clean package -Pdocker
```
Constrói a imagem Docker da aplicação pelo Maven  

## Subir aplicação
```sh
docker compose up
```

## Finalizar aplicação
```sh
docker compose down
```
