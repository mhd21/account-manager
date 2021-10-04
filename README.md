
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

* It's better to implement exchange rate service provider more modular

* It's better to set exchange rate api access key as a ENV variable

* Because of problems after creating jar file to access swagger endpoint, I use ```./mvnw spring-boot:run``` in docker file
