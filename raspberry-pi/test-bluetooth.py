from bluedot import BlueDot
import time
bd = BlueDot()

while True:
    x, y = 0.0, 0.0
    if bd.is_pressed:
        x, y = bd.position.x, bd.position.y
        time.sleep(0.05)
