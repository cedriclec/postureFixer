from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTShadowClient

import json



# For certificate based connection
myShadowClient = AWSIoTMQTTShadowClient("myClientID")
# For Websocket connection
# myMQTTClient = AWSIoTMQTTClient("myClientID", useWebsocket=True)
# Configurations
# For TLS mutual authentication
myShadowClient.configureEndpoint("all6qkgnylmz8.iot.us-west-2.amazonaws.com", 8883)
# For Websocket
# myShadowClient.configureEndpoint("YOUR.ENDPOINT", 443)
myShadowClient.configureCredentials("key/root-CA.crt", "key/509e2f9bc0-private.pem.key", "key/509e2f9bc0-certificate.pem.crt")
# For Websocket, we only need to configure the root CA
# myShadowClient.configureCredentials("YOUR/ROOT/CA/PATH")
myShadowClient.configureConnectDisconnectTimeout(10)  # 10 sec
myShadowClient.configureMQTTOperationTimeout(5)  # 5 sec

myShadowClient.connect()
# Create a device shadow instance using persistent subscription
myDeviceShadow = myShadowClient.createShadowHandlerWithName("arn:aws:iot:us-west-2:051896657986:thing/distanceDevice", True)
# Shadow operations
#myDeviceShadow.shadowGet(customCallback, 5)
myJSONPayload = createMyJson()
myDeviceShadow.shadowUpdate(myJSONPayload, customCallback, 5)
#myDeviceShadow.shadowDelete(customCallback, 5)
#myDeviceShadow.shadowRegisterDeltaCallback(customCallback)
#myDeviceShadow.shadowUnregisterDeltaCallback()

def createMyJson():
    myJSON = '{"state":{"desired":{"property":5}}}'
    return myJSON
