# Continuous Delivery to Kubernetes with [jib](https://github.com/GoogleContainerTools/jib#what-is-jib), [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) and [ktor](https://github.com/ktorio/ktor)  


##### preconditions
- [kubernetes](https://kubernetes.io/) (v1.13.4)
- [minikube](https://kubernetes.io/docs/setup/minikube/) (v0.35.0)
- [docker](https://www.docker.com/) (v18.09.7)
- [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) (v0.39.0)
- [ktor](https://ktor.io/)
- [kotlin](https://kotlinlang.org/) (1.2.5)
- [gradle](https://gradle.org/) (v5.1.1)
- [jib](https://github.com/GoogleContainerTools/jib) (1.6.1)

#### continuous delivery
```
skaffold dev
```
other terminal:
```
./web.sh
```
The script checks the service endpoint for code changes becoming effective. 
The script also assumes kubernetes runs on (minikube)[link auf zeile ip=$(minikube ip) ] and the web kubernetes service is already deployed.

##### screencast
[![asciicast](https://asciinema.org/a/5K9pJbQYoGuuio937cGF09UFy.svg)](https://asciinema.org/a/5K9pJbQYoGuuio937cGF09UFy?t=16)

#### run application with jib & docker
```
./gradlew jibDockerBuild && docker run --rm -p 8080:8080 ktor01:1.0-SNAPSHOT
# specify docker image name
./gradlew jibDockerBuild --image=myimagename && docker run --rm -p 8080:8080 myimagename
```
access the endpoint:
```
curl http://0.0.0.0:8080
```


#### run application

```
./gradlew run
```        
access the endpoint:
```
curl http://0.0.0.0:8080
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

#### blog post
- [Deploy Kotlin Applications to Kubernetes without Dockerfiles on lotharschulz.info](https://www.lotharschulz.info/2019/10/09/deploy-kotlin-applications-to-kubernetes-without-dockerfiles/‎)
- [Kotlin Continuous Delivery to Kubernetes on lotharschulz.info](https://www.lotharschulz.info/2019/02/17/Kotlin-Continuous-Delivery-to-Kubernetes/)

#### further reading
- [Ktor](github.com/ktorio/ktor)
- [Jib](github.com/GoogleContainerTools/jib)
- [Skaffold](github.com/GoogleContainerTools/skaffold/)
- [Jib 1.0.0 is GA—building Java Docker images has never been easier](https://cloud.google.com/blog/products/application-development/jib-1-0-0-is-ga-building-java-docker-images-has-never-been-easier)
- [What is Jib?](https://github.com/GoogleContainerTools/jib#what-is-jib)
- [Containerize your Gradle Java project](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Containerize a Ktor application with Jib](https://github.com/GoogleContainerTools/jib/tree/master/examples/ktor)

