#!/bin/bash

docker build -t gearmax-api:latest .
docker tag gearmax-api:latest y82cheng/gearmax-api:latest
docker push y82cheng/gearmax-api:latest