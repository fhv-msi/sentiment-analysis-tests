#!/bin/bash

TESTINGBOT_AUTH="2c91862c6903ea06f00aac0a191956b3:a4fcd27dde11688cf50414aac5357136"

RESPONSE=`curl --silent -u $TESTINGBOT_AUTH https://api.testingbot.com/v1/tests/ded0171b-c541-4828-bf23-716cd12194a2?skip_fields=logs,thumbs,steps`


echo $RESPONSE | python -c 'import json, sys; print(json.loads(sys.stdin.read())["video"]);'
