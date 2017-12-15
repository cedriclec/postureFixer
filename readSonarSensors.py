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

from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import logging
import time
import argparse
import RPi.GPIO as GPIO

import json
import datetime
from readSensorOnce import readSensorDistance

# Define GPIO to use on Pi
# have two sensors on computer (top), and two behind user attached to chair (bottom)
# will take average of both sensor readings
# consider only have one sensor on top 
GPIO_TRIGGER_TOP1 = 23
GPIO_ECHO_TOP1 = 24

#GPIO_TRIGGER_TOP2 = 23 
#GPIO_ECHO_TOP2 = 24 

GPIO_TRIGGER_DOWN1 = 5 
GPIO_ECHO_DOWN1 = 6

#GPIO_TRIGGER_DOWN2 = 17
#GPIO_ECHO_DOWN2 = 27


# Custom MQTT message callback
def customCallback(client, userdata, message):
    print("Received a new message: ")
    print(message.payload)
    print("from topic: ")
    print(message.topic)
    print("--------------\n\n")

def createJsonSensorsDistance(distanceTop, distanceBottom, userId = 1):
    #Limited to three number after virgule
    distanceTop = round(distanceTop, 3)
    distanceBottom = round(distanceBottom, 3)
    jsonSensors = json.dumps({"dateTime" : str(datetime.datetime.now().strftime("%Y%m%d%H%M%S")), "userID" : str(userId), "Top" : str(distanceTop), "Bottom" : str(distanceBottom)})
    return jsonSensors

host = "all6qkgnylmz8.iot.us-west-2.amazonaws.com" #args.host
rootCAPath = "key/root-CA.crt" #args.rootCAPath
certificatePath = "key/509e2f9bc0-certificate.pem.crt" #args.certificatePath
privateKeyPath = "key/509e2f9bc0-private.pem.key" #args.privateKeyPath
useWebsocket = False #args.useWebsocket
clientId = "postureUser" #args.clientId
topic = "distanceSensors/distance" #args.topic

#if args.useWebsocket and args.certificatePath and args.privateKeyPath:
  #  parser.error("X.509 cert authentication and WebSocket are mutual exclusive. Please pick one.")
 #   exit(2)

#if not args.useWebsocket and (not args.certificatePath or not args.privateKeyPath):
  #  parser.error("Missing credentials for authentication.")
 #   exit(2)

# Configure logging
logger = logging.getLogger("AWSIoTPythonSDK.core")
logger.setLevel(logging.DEBUG)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# Init AWSIoTMQTTClient
myAWSIoTMQTTClient = None
if useWebsocket:
    myAWSIoTMQTTClient = AWSIoTMQTTClient(clientId, useWebsocket=True)
    myAWSIoTMQTTClient.configureEndpoint(host, 443)
    myAWSIoTMQTTClient.configureCredentials(rootCAPath)
else:
    myAWSIoTMQTTClient = AWSIoTMQTTClient(clientId)
    myAWSIoTMQTTClient.configureEndpoint(host, 8883)
    myAWSIoTMQTTClient.configureCredentials(rootCAPath, privateKeyPath, certificatePath)

# AWSIoTMQTTClient connection configuration
myAWSIoTMQTTClient.configureAutoReconnectBackoffTime(1, 32, 20)
myAWSIoTMQTTClient.configureOfflinePublishQueueing(-1)  # Infinite offline Publish queueing
myAWSIoTMQTTClient.configureDrainingFrequency(2)  # Draining: 2 Hz
myAWSIoTMQTTClient.configureConnectDisconnectTimeout(10)  # 10 sec
myAWSIoTMQTTClient.configureMQTTOperationTimeout(5)  # 5 sec

# Connect and subscribe to AWS IoT
myAWSIoTMQTTClient.connect()
myAWSIoTMQTTClient.subscribe(topic, 1, customCallback)
#time.sleep(3)

# Publish to the same topic in a loop forever

distanceTop = 0
distanceBottom = 0
tempDistanceTop = 0
tempDistanceBottom = 0

sensorReadingCounter = 0 
goodSensorReadingCounter = 0
# average sensor readings by taking multiple times
while 1: 
        while sensorReadingCounter<6:
                tempDistanceTop = readSensorDistance("TOP1")
                tempDistanceBottom = readSensorDistance("BOTTOM1")    
                if tempDistanceTop<300 and tempDistanceBottom<300:
                        distanceTop += tempDistanceTop
                        distanceBottom += tempDistanceBottom
                        goodSensorReadingCounter = goodSensorReadingCounter+1
                sensorReadingCounter=sensorReadingCounter+1
                #if badSensorReadingCounter > 3:
                        #raise Exception("Too many bad sensor readings. Reposition sensors and try again.")

        distanceTopAvg = distanceTop / goodSensorReadingCounter 
        distanceBottomAvg = distanceBottom / goodSensorReadingCounter
        sensorReadingCounter = 0
        goodSensorReadingCounter = 0
        distanceTop = 0
        distanceBottom = 0 
        JSONPayload = createJsonSensorsDistance(distanceTopAvg, distanceBottomAvg)
        myAWSIoTMQTTClient.publish(topic, JSONPayload, 1)
        print(JSONPayload)
