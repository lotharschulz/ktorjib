#!/bin/bash
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

# simple test script to check the webservice endpoint deployed on eks service
# this script assumes the _web_ kubernetes service (https://github.com/lotharschulz/ktorjib/blob/master/k8s/service.yaml#L4)
# is already deployed.

while :
do
  endpoint=$(kubectl describe svc web -n ktorjib | grep Endpoints:)
  ip_port=${endpoint:26:28} # ending 18 was enough in a lot of cases
  echo "curl http://$ip_port: "
  curl http://$ip_port || true
  echo " "
  sleep 4s
done