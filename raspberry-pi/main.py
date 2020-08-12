#!/usr/bin/env python

from pymultiwii import MultiWii
from bluedot import BlueDot
import threading
import time

board = MultiWii("/dev/ttyACM0")
print("Flight Controller connected!")

leftStick = BlueDot(port=1)
rightStick = BlueDot(port=2)

while not leftStick.is_connected or not rightStick.is_connected:
    time.sleep(0.1)

print("Android Controller connected!")

while True:
    # calculate roll, pitch, yaw and throttle from dpad positions
    x, y = 0.0, -1.0
    if leftStick and leftStick.is_pressed:
        x, y = leftStick.position.x, leftStick.position.y
    throttle = (y + 1.0) / 2.0
    rudder = x ** 3

    x, y = 0.0, 0.0
    if rightStick and rightStick.is_pressed:
        x, y = rightStick.position.x, rightStick.position.y
    aileron = x
    elevator = -y

    # roll, pitch, yaw, throttle, aux1, aux2, aux3, aux4
    # each from 1000 to 2000
    rcCommandData = [
        int(aileron * 500 + 1500),
        int(elevator * 500 + 1500),
        int(throttle * 1000 + 1000),
        int(rudder * 500 + 1500),
        2000, 1000, 1000, 1000]

    # send rc command
    board.sendCMD(16, MultiWii.SET_RAW_RC, rcCommandData, '8H')
    time.sleep(0.025)

    # print board attitude
    board.getData(MultiWii.ATTITUDE)
    print(board.attitude)
    time.sleep(0.025)
