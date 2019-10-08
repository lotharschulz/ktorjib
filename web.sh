#!/bin/bash
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail
IFS=$'\n\t'

while :
do
        echo "curl http://127.0.0.1:8080/: "
        curl http://127.0.0.1:8080/ || true
        echo " "
        sleep 4s
done