import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BCM)

TRIG = 23
ECHO = 24

print("Distance Measurement in Progress")

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)
GPIO.output(TRIG,False)

print ("Waiting for sensor to settle")

i = 0
badCounter = 0
while i<5:
	time.sleep(5)
	GPIO.output(TRIG, True)
	time.sleep(0.00001)
	GPIO.output(TRIG,False)

	while GPIO.input(ECHO)==0:
		pulse_start = time.time()

	while GPIO.input(ECHO)==1:
		pulse_end = time.time()

	pulse_duration = pulse_end - pulse_start

	distance = pulse_duration * 17150
	distance = round(distance, 2)

	print("Distance: "+str(distance)+"cm")
	if distance<300:
                i = i+1
        else:
                badCounter = badCounter+1
        
        if badCounter>2:
                GPIO.cleanup()
                raise Exception("Too many bad readings.")

GPIO.cleanup()
print("Program finished executing gracefully.")
