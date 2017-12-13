#!/usr/bin/python
#+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
#|R|a|s|p|b|e|r|r|y|P|i|-|S|p|y|.|c|o|.|u|k|
#+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
#
# ultrasonic_1.py
# Measure distance using an ultrasonic module
#
# Author : Matt Hawkins
# Date   : 09/01/2013

# Import required Python libraries
import time
import RPi.GPIO as GPIO

# Use BCM GPIO references
# instead of physical pin numbers
GPIO.setmode(GPIO.BCM)


# Define GPIO to use on Pi
GPIO_TRIGGER_TOP1 = 23
GPIO_ECHO_TOP1 = 24

#GPIO_TRIGGER_TOP2 = 23 #TODO
#GPIO_ECHO_TOP2 = 24 #TODO

GPIO_TRIGGER_DOWN1 = 5 
GPIO_ECHO_DOWN1 = 6 

#GPIO_TRIGGER_DOWN2 = 17 #TODO
#GPIO_ECHO_DOWN2 = 27 #TODO

def readSensorDistance(sensorName):
  GPIO.setwarnings(False)
  print ("Ultrasonic Measurement ", sensorName)
  # Use BCM GPIO references
  # instead of physical pin numbers
  GPIO.setmode(GPIO.BCM)

  (gpioTrigger, gpioEcho) = getGpioSensorInfo(sensorName)
  # Set pins as output and input
  print("gpioTrigger ", gpioTrigger)
  GPIO.setup(gpioTrigger,GPIO.OUT)  # Trigger
  GPIO.setup(gpioEcho,GPIO.IN)      # Echo

  # Set trigger to  (Low)
  GPIO.output(gpioTrigger, False)

  # Allow module to settle
  time.sleep(1)

  # Send 10us pulse to trigger
  GPIO.output(gpioTrigger, True)
  time.sleep(0.00001)
  GPIO.output(gpioTrigger, False)
  start = time.time()
  while GPIO.input(gpioEcho)==0:
    start = time.time()

  while GPIO.input(gpioEcho)==1:
    stop = time.time()

  # Calculate pulse length
  elapsed = stop-start

  # Distance pulse travelled in that time is time
  # multiplied by the speed of sound (cm/s)
  distance = elapsed * 34000

  # That was the distance there and back so halve the value
  distance = distance / 2

  print("Distance : %.1f" % distance)
  
  # Reset GPIO settings
  GPIO.cleanup()

  return distance

def getGpioSensorInfo(sensorName):
  gpioTrigger = 0
  gpioEcho = 0
  if sensorName == "TOP1":
    print ("Read Top1 Sensor")
    gpioTrigger = GPIO_TRIGGER_TOP1
    gpioEcho = GPIO_ECHO_TOP1
  elif sensorName == "TOP2":
    print ("Read Top2 Sensor")
    gpioTrigger = GPIO_TRIGGER_TOP2
    gpioEcho = GPIO_ECHO_TOP2
  elif sensorName == "BOTTOM1":
    print ("Read BOTTOM1 Sensor")
    gpioTrigger = GPIO_TRIGGER_DOWN1
    gpioEcho = GPIO_ECHO_DOWN1
  elif sensorName == "BOTTOM2":
    print ("Read BOTTOM2 Sensor")
    gpioTrigger = GPIO_TRIGGER_DOWN2
    gpioEcho = GPIO_ECHO_DOWN2    
  else:
      print ("Error! Sensor name not good.")
    
  return (gpioTrigger, gpioEcho)


#Test function
#ireadSensorDistance("TOP1")
#readSensorDistance("TOP2")
#readSensorDistance("BOTTOM1")
#readSensorDistance("BOTTOM2")
