from bluedot import BlueDot
import time
leftStick = BlueDot()

while True:
    x, y = 0.0, -1.0
    if leftStick.is_pressed:
        x, y = leftStick.position.x, leftStick.position.y
    throttle = (y + 1.0) / 2.0
    rudder = x ** 3

    print('throttle: ' + str(throttle) + '\t rudder: ' + str(rudder))
    time.sleep(0.05)
