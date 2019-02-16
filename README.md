# [ktor](https://github.com/ktorio/ktor) app continuously delivered to kubernetes  


preconditions
- [minikube](https://kubernetes.io/docs/setup/minikube/)
- [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold)
- [ktor](https://ktor.io/)
- [gradle](https://gradle.org/)

continuous delivery
```
skaffold dev
# access ktor web app on 'http://127.0.0.1:8080/'
# change the ktor app and reload 'http://127.0.0.1:8080/' 
```


run application with jib & docker
```
./gradlew jibDockerBuild && docker run --rm -p 8080:8080 ktor01:1.0-SNAPSHOT
```

run application

```
./gradlew run
```

test application

```
./gradlew test
```

clean up
```
./gradlew clean
docker rmi $(docker images -q)
```

further reading
- [Jib 1.0.0 is GA—building Java Docker images has never been easier](https://cloud.google.com/blog/products/application-development/jib-1-0-0-is-ga-building-java-docker-images-has-never-been-easier)
- [What is Jib?](https://github.com/GoogleContainerTools/jib#what-is-jib)
- [Containerize your Gradle Java project](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Containerize a Ktor application with Jib](https://github.com/GoogleContainerTools/jib/tree/master/examples/ktor)

