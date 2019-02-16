# Continuous Delivery to Kubernetes with [jib](https://github.com/GoogleContainerTools/jib#what-is-jib), [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) and [ktor](https://github.com/ktorio/ktor)  


##### preconditions
- [minikube](https://kubernetes.io/docs/setup/minikube/)
- [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold)
- [ktor](https://ktor.io/)
- [gradle](https://gradle.org/)

#### continuous delivery
```
skaffold dev
# access ktor web app on 'http://127.0.0.1:8080/'
# change the ktor app and reload 'http://127.0.0.1:8080/' 
```

[![Ktor App Continuous Delivery](http://img.youtube.com/vi/T-Ed_tbi1f8/0.jpg)](https://www.youtube.com/watch?v=T-Ed_tbi1f8 "Ktor App Continuous Delivery")

#### run application with jib & docker
```
./gradlew jibDockerBuild && docker run --rm -p 8080:8080 ktor01:1.0-SNAPSHOT
```

#### run application

```
./gradlew run
```

#### test application

```
./gradlew test
```

#### clean up
```
./gradlew clean
docker rmi $(docker images -q)
```

further reading
- [Jib 1.0.0 is GA—building Java Docker images has never been easier](https://cloud.google.com/blog/products/application-development/jib-1-0-0-is-ga-building-java-docker-images-has-never-been-easier)
- [What is Jib?](https://github.com/GoogleContainerTools/jib#what-is-jib)
- [Containerize your Gradle Java project](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Containerize a Ktor application with Jib](https://github.com/GoogleContainerTools/jib/tree/master/examples/ktor)

