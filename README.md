# Continuous Delivery to Kubernetes with [Kotlin](https://kotlinlang.org/), [Ktor](https://github.com/ktorio/ktor), [Gradle](https://gradle.org/), [Jib](https://github.com/GoogleContainerTools/jib#what-is-jib), [Skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) and [Kubernetes](https://kubernetes.io/) ([EKS](https://aws.amazon.com/eks/) & [Minikube](https://kubernetes.io/docs/setup/minikube/))  

## Preconditions
- [jq](https://stedolan.github.io/jq/download/)
- [Kubernetes](https://kubernetes.io/) (v1.18.0 on minikube, 1.15 on EKS)
- [Minikube](https://kubernetes.io/docs/setup/minikube/) (v1.9.2)
  - one VM provider [VirtualBox](https://www.virtualbox.org/)
- [Docker](https://www.docker.com/) (v19.03.8)
- [Skaffold](https://skaffold.dev/docs/getting-started/#installing-skaffold) (v1.8.0)
- [Java 12](https://jdk.java.net/12/)
  - [community installation options not only for mac os](https://stackoverflow.com/questions/52524112/how-do-i-install-java-on-mac-osx-allowing-version-switching)
- [Kotlin](https://kotlinlang.org/) (1.3.72)
- [Ktor](https://ktor.io/) (1.3.2)
- [Gradle](https://gradle.org/) (v6.2.2)
- [Jib](https://github.com/GoogleContainerTools/jib) (2.1.0)
  - [Jib Gradle Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)

## Continuous delivery
```
skaffold dev
```
other terminal:
```
./web-eks.sh # EKS on AWS
# or
./web.sh # local minikube
```
The bash script checks the service endpoint for code changes becoming effective. 
The `web-eks.sh` script assumes kubernetes runs on [EKS](web-eks.sh#L12) and the [web kubernetes service is already deployed](web-eks.sh#L13).
The `web.sh` script assumes kubernetes runs on [minikube](web.sh#L9) and the [web kubernetes service is already deployed](web.sh#L10).

### Screencasts

#### EKS on AWS
[![asciicast](https://asciinema.org/a/321763.svg)](https://asciinema.org/a/321763?t=20)

#### Minikube in local
[![asciicast](https://asciinema.org/a/vfx729qpylmfdroBTXJmTH2bw.svg)](https://asciinema.org/a/vfx729qpylmfdroBTXJmTH2bw?t=14)

### Run application

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

### Test application

```
./gradlew test
```

### Clean up with Gradle
```
./gradlew clean
docker rmi $(docker images -q)
```

## Skaffold deployment to [EKS](https://aws.amazon.com/eks/) with [ECR](https://aws.amazon.com/ecr/) ([AWS](https://aws.amazon.com/))

The steps below assume an EKS Kubernetes cluster is in place. The setup might be similar to the one described in [ALB INGRESS Controller CrashLoopBackOffs in AWS EKS on FARGATE](https://www.lotharschulz.info/2020/01/29/alb-ingress-controller-crashloopbackoffs-in-aws-eks-on-fargate/).
The steps below assume an ENV variable `$CLUSTER_NAME` holding the actual Kubernetes cluster name is in place in the terminal session.

1. Define additional ENV variables for later use.

```
ENDPOINT_URL=$(aws eks describe-cluster --name $CLUSTER_NAME --query cluster.endpoint --output text)
echo $ENDPOINT_URL
CA_CERT=$(aws eks describe-cluster --name $CLUSTER_NAME --query cluster.certificateAuthority.data --output text)
echo $CA_CERT
AWS_PROFILE=test
echo $AWS_PROFILE
AWS_REGION=$(aws configure get region)
echo $AWS_REGION
```

2. [Install aws-iam-authenticator](https://docs.aws.amazon.com/eks/latest/userguide/install-aws-iam-authenticator.html)

3. configure `kubectl` with EKS cluster via `KUBECONFIG` ENV variable
```
ll ~/.kube
# in case the folder does not exists
mkdir -p ~/.kube

cat << KBCFG > ~/.kube/config-${CLUSTER_NAME}
apiVersion: v1
clusters:
- cluster:
    certificate-authority-data: $CA_CERT
    server: $ENDPOINT_URL
  name: $CLUSTER_NAME
contexts:
- context:
    cluster: $CLUSTER_NAME
    user: $CLUSTER_NAME
  name: $CLUSTER_NAME
current-context: $CLUSTER_NAME
kind: Config
preferences: {}
users:
- name: $CLUSTER_NAME
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      args:
      - --region
      - $AWS_REGION
      - eks
      - get-token
      - --cluster-name
      - $CLUSTER_NAME
      command: aws
      env:
      - name: AWS_PROFILE
        value: $AWS_PROFILE
KBCFG
export KUBECONFIG=$KUBECONFIG:~/.kube/config-${cluster_name}
echo $KUBECONFIG
```

4. TODO

```
# assuming aws cli v2 - https://aws.amazon.com/blogs/developer/aws-cli-v2-is-now-generally-available/
aws --version

# ecr login (https://github.com/aws/aws-cli/issues/4962)
echo $(aws ecr get-login-password)|docker login --password-stdin --username AWS https://$(aws sts get-caller-identity --query 'Account' --output text).dkr.ecr.${AWS_REGION}.amazonaws.com

# create or use an existing ECR repository
export ECRREPO_NAME=[ktorjib]
echo $ECRREPO_NAME
export ECRREPO_URI=$(aws ecr describe-repositories --repository-names $ECRREPO_NAME --query 'repositories[0].repositoryUri' --output text)
echo $ECRREPO_URI

export KUBE_SECRET_LABEL=$(aws sts get-caller-identity --query 'Account' --output text)--${AWS_REGION}--${AWS_PROFILE}--ecr--registry--secret
echo $KUBE_SECRET_LABEL
export KUBE_SECRET_PASSWORD=$(aws ecr get-authorization-token --output text --query 'authorizationData[0].authorizationToken' | base64 -d | cut -d: -f2)
echo $KUBE_SECRET_PASSWORD
KUBE_SECRET_EMAIL=[me@you.com]
echo $KUBE_SECRET_EMAIL

# configure kubectl to log into ECR
kubectl delete secret --ignore-not-found $KUBE_SECRET_LABEL
kubectl create secret docker-registry $KUBE_SECRET_LABEL \
 --docker-server=https://${ECRREPO_URI} \
 --docker-username=AWS \
 --docker-password="${KUBE_SECRET_PASSWORD}" \
 --docker-email="${KUBE_SECRET_EMAIL}"

# set up kubernetes namespace
export KTORJIB_K8S_NAMESPACE=ktorjib
echo ${KTORJIB_K8S_NAMESPACE}
kubectl create namespace ${KTORJIB_K8S_NAMESPACE}
kubectl get namespaces

# set up skaffold config
cat << SKFLDCFG > skaffold.yaml
# inspired by https://github.com/GoogleContainerTools/skaffold/blob/master/examples/jib-gradle/skaffold.yaml @ 2020 04 19
apiVersion: skaffold/v2beta2
kind: Config
build:
  artifacts:
    #- image: gcr.io/ktor-jib/kjib-image
    - image: ${ECRREPO_URI}
      jib: {}
SKFLDCFG

# copy the skaffold config file for backup
cp skaffold.yaml skaffold-ecr.yaml_

# start skaffold flow 
# kubernetes namespace can be specified with ENV var `SKAFFOLD_NAMESPACE` or cli parameter `--namespace`
skaffold dev --namespace KTORJIB_K8S_NAMESPACE
```

### links
- https://github.com/stelligent/skaffold_on_aws
- https://github.com/aws-samples/aws-microservices-deploy-options/blob/master/skaffold.md

## Skaffold deployment to Minikube
```
minikube start --v=5 --kubernetes-version=1.18.0
# start skaffold flow
skaffold dev
```

### Notes

#### Java versions

```
FAILURE: Build failed with an exception
....
* What went wrong:
Script compilation error:

  Line 29: java.sourceCompatibility = JavaVersion.VERSION_13
                                                  ^ Unresolved reference: VERSION_13

1 error
```

```
FAILURE: Build failed with an exception.
...
* What went wrong:
Script compilation error:

  Line 29: java.sourceCompatibility = JavaVersion.VERSION_14
                                                  ^ Unresolved reference: VERSION_14

1 error
```

```kotlin
// works:
...
JavaVersion.VERSION_12
...
image = "openjdk:14"

// but for consistency
...
JavaVersion.VERSION_12
...
image = "openjdk:12"
```

##### links
- https://github.com/GoogleContainerTools/jib/search?q=java+14&type=Code
- https://github.com/GoogleContainerTools/jib/blob/master/jib-core/CHANGELOG.md
- https://github.com/GoogleContainerTools/jib/issues/2015
- https://github.com/GoogleContainerTools/jib/pull/2017/files
- https://github.com/GoogleContainerTools/jib/issues/2015#issuecomment-534168864
- https://asm.ow2.io/versions.html
- java 8 or 11 -> https://github.com/GoogleContainerTools/distroless/tree/master/java
- https://github.com/GoogleContainerTools/distroless/tree/master/java#image-contents
- https://github.com/GoogleContainerTools/distroless/blob/master/examples/java/Dockerfile
- https://console.cloud.google.com/gcr/images/distroless/GLOBAL/java?gcrImageListsize=30&gcrImageListsort=-uploaded (version 11)

# Blog posts
- [Deploy Kotlin Applications to Kubernetes without Dockerfiles on lotharschulz.info](https://www.lotharschulz.info/2019/10/17/deploy-kotlin-applications-to-kubernetes-without-dockerfiles/)
- [Kotlin Continuous Delivery to Kubernetes on lotharschulz.info](https://www.lotharschulz.info/2019/02/17/Kotlin-Continuous-Delivery-to-Kubernetes/)

# Further reading
- [Ktor](github.com/ktorio/ktor)
- [Jib](github.com/GoogleContainerTools/jib)
- [Skaffold](github.com/GoogleContainerTools/skaffold/)
- [Jib 1.0.0 is GA—building Java Docker images has never been easier](https://cloud.google.com/blog/products/application-development/jib-1-0-0-is-ga-building-java-docker-images-has-never-been-easier)
- [What is Jib?](https://github.com/GoogleContainerTools/jib#what-is-jib)
- [Containerize your Gradle Java project](https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin)
- [Containerize a Ktor application with Jib](https://github.com/GoogleContainerTools/jib/tree/master/examples/ktor)
