
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