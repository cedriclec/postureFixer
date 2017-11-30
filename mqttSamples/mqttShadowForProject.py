'''
/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
 '''

from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTShadowClient
import logging
import time
import json
import argparse

from readSensorOnce import readSensorDistance




# Custom Shadow callback
def customShadowCallback_Update(payload, responseStatus, token):
    # payload is a JSON string ready to be parsed using json.loads(...)
    # in both Py2.x and Py3.x
    if responseStatus == "timeout":
        print("Update request " + token + " time out!")
    if responseStatus == "accepted":
        payloadDict = json.loads(payload)
        print("~~~~~~~~~~~~~~~~~~~~~~~")
        print("Update request with token: " + token + " accepted!")
        #print("property: " + str(payloadDict["state"]["desired"]["property"]))
        print("property: " + str(payloadDict["state"]["desired"]))
        print("~~~~~~~~~~~~~~~~~~~~~~~\n\n")
    if responseStatus == "rejected":
        print("Update request " + token + " rejected!")

def customShadowCallback_Delete(payload, responseStatus, token):
    if responseStatus == "timeout":
        print("Delete request " + token + " time out!")
    if responseStatus == "accepted":
        print("~~~~~~~~~~~~~~~~~~~~~~~")
        print("Delete request with token: " + token + " accepted!")
        print("~~~~~~~~~~~~~~~~~~~~~~~\n\n")
    if responseStatus == "rejected":
        print("Delete request " + token + " rejected!")

# Shadow JSON schema:
#
# Name: distanceDevice
# {
#	"state": {
#		"Top":{
#			"value":<INT VALUE>
#		},
#		"Bottom":{
#			"value":<INT VALUE>
#		}
#	}
# }

#JSONexqmple = '{"state":{"desired":{"property":' + str(loopCount) + '}}}'

def createJsonSensorsDistance(distanceTop, distanceBottom):
    #Limite to three number after virgule
    distanceTop = round(distanceTop, 3)
    distanceBottom = round(distanceBottom, 3)
    #jsonSensors = json.dumps({"state": { "Top" : { "distance" : distanceTop}, "Bottom" : { "distance" : distanceBottom} } })
    #jsonSensors = json.dumps({"state": { "Top" : distanceTop, "Bottom" : distanceBottom} })
    #jsonSensors = json.dumps({"state":{"desired":{"property": distanceTop} } } )
    jsonSensors = json.dumps({"state":{"desired":{ "Top" : distanceTop, "Bottom" : distanceBottom} } } )
    print jsonSensors
    return jsonSensors
    


host = "all6qkgnylmz8.iot.us-west-2.amazonaws.com" #args.host
rootCAPath = "key/root-CA.crt" #args.rootCAPath
certificatePath = "key/509e2f9bc0-certificate.pem.crt" #args.certificatePath
privateKeyPath = "key/509e2f9bc0-private.pem.key" #args.privateKeyPath
useWebsocket = False #args.useWebsocket
thingName = "distanceDevice" #args.thingName
clientId = "postureUser" #args.clientId

# Configure logging
logger = logging.getLogger("AWSIoTPythonSDK.core")
logger.setLevel(logging.DEBUG)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# Init AWSIoTMQTTShadowClient
myAWSIoTMQTTShadowClient = None
if useWebsocket:
    myAWSIoTMQTTShadowClient = AWSIoTMQTTShadowClient(clientId, useWebsocket=True)
    myAWSIoTMQTTShadowClient.configureEndpoint(host, 443)
    myAWSIoTMQTTShadowClient.configureCredentials(rootCAPath)
else:
    myAWSIoTMQTTShadowClient = AWSIoTMQTTShadowClient(clientId)
    myAWSIoTMQTTShadowClient.configureEndpoint(host, 8883)
    myAWSIoTMQTTShadowClient.configureCredentials(rootCAPath, privateKeyPath, certificatePath)

# AWSIoTMQTTShadowClient configuration
myAWSIoTMQTTShadowClient.configureAutoReconnectBackoffTime(1, 32, 20)
myAWSIoTMQTTShadowClient.configureConnectDisconnectTimeout(10)  # 10 sec
myAWSIoTMQTTShadowClient.configureMQTTOperationTimeout(5)  # 5 sec

# Connect to AWS IoT
myAWSIoTMQTTShadowClient.connect()

# Create a deviceShadow with persistent subscription
deviceShadowHandler = myAWSIoTMQTTShadowClient.createShadowHandlerWithName(thingName, True)

# Delete shadow JSON doc
deviceShadowHandler.shadowDelete(customShadowCallback_Delete, 5)

# Update shadow in a loop
#while True:
i = 1
while i:
#Limite to one call to avoid having too much message sent
    distanceTOP = readSensorDistance("TOP")
    distanceDOWN = 0
#   distanceDOWN = readSensorDistance("DOWN")
    JSONPayload = createJsonSensorsDistance(distanceTOP, distanceDOWN)
    print(JSONPayload)
    deviceShadowHandler.shadowUpdate(JSONPayload, customShadowCallback_Update, 5)
    time.sleep(5)
    i = 0

