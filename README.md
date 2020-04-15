# Continuous Delivery to Kubernetes with [jib](https://github.com/GoogleContainerTools/jib#what-is-jib), [skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) and [ktor](https://github.com/ktorio/ktor)  


##### preconditions
- [jq](https://stedolan.github.io/jq/download/)
- [Kubernetes](https://kubernetes.io/) (v1.13.4)
- [Minikube](https://kubernetes.io/docs/setup/minikube/) (v0.35.0)
  - one VM provider [VirtualBox](https://www.virtualbox.org/)
- [Docker](https://www.docker.com/) (v19.03.8)
- [Skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) (v0.39.0)
- [Java 12](https://jdk.java.net/12/)
  - [community installation options not only for mac os](https://stackoverflow.com/questions/52524112/how-do-i-install-java-on-mac-osx-allowing-version-switching)
- [Kotlin](https://kotlinlang.org/) (1.3.71)
- [Ktor](https://ktor.io/) (1.3.2)
- [Gradle](https://gradle.org/) (v6.2.2)
- [Jib](https://github.com/GoogleContainerTools/jib) (2.1.0)
  - [Jib Gradle Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)

#### Continuous delivery
```
skaffold dev
```
other terminal:
```
./web.sh
```
The bash script checks the service endpoint for code changes becoming effective. 
The script also assumes kubernetes runs on [minikube](web.sh#L9) and the [web kubernetes service is already deployed](web.sh#L10).

##### Screencast
[![asciicast](https://asciinema.org/a/vfx729qpylmfdroBTXJmTH2bw.svg)](https://asciinema.org/a/vfx729qpylmfdroBTXJmTH2bw?t=14)

#### Run application with Jib & Docker
```
./gradlew jibDockerBuild && docker run --rm -p 8080:8080 ktor01.1:1.1-SNAPSHOT
# specify docker image name
./gradlew jibDockerBuild --image=myimagename && docker run --rm -p 8080:8080 myimagename
```
access the endpoint:
```
curl http://0.0.0.0:8080
```


#### Run application with Gradle

```
./gradlew run
```        
access the endpoint:
```
curl http://0.0.0.0:8080
```

#### Test application

```
./gradlew test
```

#### Clean up with Gradle
```
./gradlew clean
docker rmi $(docker images -q)
```

###### notes
- https://github.com/GoogleContainerTools/jib/search?q=java+14&type=Code
- https://github.com/GoogleContainerTools/jib/blob/master/jib-core/CHANGELOG.md
- https://github.com/GoogleContainerTools/jib/issues/2015
- https://github.com/GoogleContainerTools/jib/pull/2017/files
- https://github.com/GoogleContainerTools/jib/issues/2015#issuecomment-534168864
- https://asm.ow2.io/versions.html

#### Blog posts
- [Deploy Kotlin Applications to Kubernetes without Dockerfiles on lotharschulz.info](https://www.lotharschulz.info/2019/10/17/deploy-kotlin-applications-to-kubernetes-without-dockerfiles/)
- [Kotlin Continuous Delivery to Kubernetes on lotharschulz.info](https://www.lotharschulz.info/2019/02/17/Kotlin-Continuous-Delivery-to-Kubernetes/)

#### Further reading
- [Ktor](github.com/ktorio/ktor)
- [Jib](github.com/GoogleContainerTools/jib)
- [Skaffold](github.com/GoogleContainerTools/skaffold/)
- [Jib 1.0.0 is GA—building Java Docker images has never been easier](https://cloud.google.com/blog/products/application-development/jib-1-0-0-is-ga-building-java-docker-images-has-never-been-easier)
- [What is Jib?](https://github.com/GoogleContainerTools/jib#what-is-jib)
- [Containerize your Gradle Java project](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Containerize a Ktor application with Jib](https://github.com/GoogleContainerTools/jib/tree/master/examples/ktor)
