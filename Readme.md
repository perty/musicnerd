# Music Nerd

Get that info on that artist with this remarkable service that collects data from not less than 4 services on the internet!

## Linking information

Given the `MBID (MusicBrainz Identifier)` MusicNerd will collect the information from `MusicBrainz` database. From that information the `Wikidata` service is queried to get the story on `Wikipedia`.

The `MusicBrainz` response is also used to find the cover art from `Cover Art Archive` database.

### Sequence diagram

The diagram illustrates the order of the service calls. Note the parallellism in the last calls. The `Wikidata` and `Wikipedia` calls are executed in parallell with the call to `Cover Art Archive`.

```mermaid
sequenceDiagram
actor user
participant MN as MusicNerd
participant MB as MusicBrainz
participant WD as Wikidata
participant WP as Wikipedia
participant CA as Cover Art Archive
user->>MN: GET /musify/music-artist/details/{mbid} 
MN->>MB: GET artist/{mbid}
MB-->>MN: artist-urls
par MusicNerd to Wikidata and Wikipedia
    MN->>WD: GET Entity data
    WD-->>MN: wikipedia link
    MN->>WP: GET page/summary
    WP-->>MN: summary
and MusicNerd to Cover Art Archive
    loop every album
        MN->>CA: GET /release-group
        CA-->>MN: cover arts
    end
end
MN-->>user: MusicNerd response
```

## Architecture

The service serves the needs of users from all over the world and needs to be there all the time. Therefore, the architecture must have the qualities to match these business goals.

### Scalability
The service is deployed behind auto-scaling load balancer that increase and decrease the number of instances as the load varies.

### Performance
Although the response times depends on the services it uses, the `Cover Art Archive` service is called in parallell with `Wikidata` and `Wikipedia`. 

### Availability
The service is deployed in several availability zones across the world to ensure maximum availability.

The minimum number of instances in each zone is two, to ensure availability during maintenance.

### Security
The correctness and availability ot the information presented by the service is depending on the services used. 

MusicNerd will respond with a `Gateway Timeout (504)` when any of those services is not available.

All information is public and users are anonymous.

### Testability
The service is depending on 4 other services and those needs to be simulated in some way. 
We use `MockWebServer` for this.

### Maintainability
The service logs all errors and alarms are set up to monitor the logs. The alarms are sent to a dedicated Slack channel.

The service can run on a local machine and still present production data as it is public. Therefore, any issues can be examined locally.

### Usability
The service exposes a REST API publicly which is great, from an architectural standpoint on usability.
From a human user perspective, not so much.

## Design choices

To create a service that is as responsive as possible, we selected `WebFlux` of `Spring boot`. This is allows for 
reactive programming, specifically for non-blocking calls. 

It turns out that the time spent looking up cover art, overshadows the time used to get Wikipedia data, so the gain is 
very small. Further investigation may reveal improvements.

Java `record` was used instead of classes where fit.

Code is organized after the services that is used. Each package represents a port and adapter for that service. Only necessary records and classes are declared public.

The main service depends on all four packages.

### Error handling
The handling of errors is lacking heavily, see below for an example of a request where the cover art is not found. 

## How to run the service

Start the service locally from the repository root with `mvn spring-boot:run`.

The service runs at port 8080. Some requests to try:
- http://localhost:8080/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e (Michael Jackson)
- http://localhost:8080/musify/music-artist/details/5441c29d-3602-4898-b1a1-b77fa23b8e50 (David Bowie)
- http://localhost:8080/musify/music-artist/details/510fcf21-a1f3-40af-9087-40593af86f7f (Barns Courtnety - results in an error)

Response times may be very long, about 20 seconds.
