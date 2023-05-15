#!/bin/bash
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

# simple test script to check the webservice endpoint deployed on minikube
# this script assumes the web kubernetes service (https://github.com/lotharschulz/ktorjib/blob/master/k8s/service.yaml#L4)
# is already deployed.
ip=$(minikube ip)
port=$(kubectl get svc web -o=jsonpath='{.spec.ports[0].nodePort}')

while :
do
        now=$(date +"%T")
        echo "$now curl http://$ip:$port: "
        curl http://$ip:$port || true
        echo -e "\n"
        sleep 4s
done


# https://stackoverflow.com/questions/52277019/how-to-fix-vm-issue-with-minikube-start/52278212#52278212
# https://stackoverflow.com/questions/60710171/minikube-ip-is-not-reachable/63243909#63243909