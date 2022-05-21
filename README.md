Za pokretanje uz pretpostavku da imate instaliranu javu 11, maven i docker. Iz root projekta izvršiti:

```
docker-compose up -d
mvn package
java -jar target/tv-api-0.0.1-SNAPSHOT.jar
```
