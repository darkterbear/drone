from bluedot import BlueDot
import time
leftStick = BlueDot(port=1)
rightStick = BlueDot(port=2)

while True:
    x, y = 0.0, -1.0
    if leftStick.is_pressed:
        x, y = leftStick.position.x, leftStick.position.y
    throttle = (y + 1.0) / 2.0
    rudder = x ** 3

    x, y = 0.0, 0.0
    if rightStick.is_pressed:
        x, y = rightStick.position.x, rightStick.position.y
    aileron = x
    elevator = -y

    print('thr: ' + str(round(throttle, 4)) + '\t rud: ' + str(round(rudder, 4)) +
          '\t ail: ' + str(round(aileron, 4)) + '\t ele: ' + str(round(elevator, 4)))
    time.sleep(0.05)
