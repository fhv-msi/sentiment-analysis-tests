#!/opt/pyenv/shims/python
import json, sys, urllib, os;

url = "https://" + os.environ['TESTINGBOT_CREDENTIALS'] + "@api.testingbot.com/v1/tests?count=10?skip_fields=logs,thumbs,steps"
jsonurl = urllib.urlopen(url)
response = json.loads(jsonurl.read())
print response
print "***************************"

# get the video link of the latest test with the name "User interaction with history"
for test in response['data']:
    print test
    print "############################"
    if test['name'] == 'User interaction with history' and test['state'] == 'COMPLETE':
        print test['video']
        # download the Video
        urllib.urlretrieve (test['video'], "walk_through.mp4")
        sys.exit(0)
