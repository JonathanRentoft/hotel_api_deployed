## Hotel REST Exercise

This is a simple REST API for a hotel booking system. 
It is implemented using Javalin and JPA

It is prepared for [deployment following the tuturials here](https://dat3cph.github.io/material/deployment/exercises/full-pipeline/).

In case you need to set up a Droplet at Digital Ocean, 
you can use this link for tutorials: [Digital Ocean tutorials](https://dat3cph.github.io/material/toolbox/deployment/digitalocean-signup/)

![Hotel](./docs/bates_hotel.jpg)

### How to run

1. Create a database in your local Postgres instance called `hotel`
2. Run the main method in the config.Populate class to populate the database with some data
3. Run the main method in the Main class to start the server on port 7070
4. See the routes in your browser at `http://localhost:7070/routes`
5. Request the `http://localhost:7070/hotels` endpoint in your browser to see the list of hotels and rooms
6. Use the dev.http file to test the routes, GET/POST/PUT/DELETE requests are available

## Docker commands

```bash
docker-compose up -d
docker-compose down
docker logs -f  watchtower
docker logs watchtower
docker logs hotelAPI
docker logs db
docker container ls
docker rmi <image_id>
docker stop <container_id>
docker rm <container_id>
```

