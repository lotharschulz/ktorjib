#!/bin/bash
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

# simple test script to check the webservice endpoint deployed on minikube
ip=$(minikube ip)
port=$(kubectl get svc web -o json | jq '.spec? | .ports? | .[] | .nodePort?')

while :
do
        echo "curl http://$ip:$port/: "
        curl http://$ip:$port/ || true
        echo " "
        sleep 4s
done