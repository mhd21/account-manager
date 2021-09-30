
# Build and Run

  

* You should install docker and docker-compose

  

* To build application run:

  

```

sh build.sh

```

  

* After building, you can run the following:

  

```

docker-compose up

```

# Documentation

After running the application you can access the documentation by following url:

http://localhost:8080/swagger-ui/index.html

* http://localhost:8080 is the default endpoint


## Note:
* I didn't consider foriegn keys and model relationships just for simplicity

* Meybe it was better to use postgres uuid function

* Because of the limitation of exchange rate api, I just call the api for USD to EUR

* It's better to implement exchange rate service provider more modular

* It's better to set exchange rate api access key as a ENV variable

* Because problems after creating jar file to access swagger endpoint, I use ```./mvnw spring-boot:run``` in docker file
