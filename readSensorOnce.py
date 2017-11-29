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
SENSOR_TOP = 1
GPIO_TRIGGER_TOP = 23
GPIO_ECHO_TOP = 24

GPIO_TRIGGER_DOWN = 23 #TODO
GPIO_ECHO_DOWN = 24 #TODO
DOWN = 2

def readSensorDistance(sensorName):
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
  time.sleep(0.5)

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
  gpioTrigger = GPIO_TRIGGER_DOWN
  gpioEcho = GPIO_ECHO_DOWN
  if sensorName == "TOP":
    print ("Read Top Sensor")

    gpioTrigger = GPIO_TRIGGER_TOP
    gpioEcho = GPIO_ECHO_TOP
  else:
      print ("Read Down Sensor")
    
  return (gpioTrigger, gpioEcho)

    
readSensorDistance("TOP")
