#!/usr/bin/env python

from pymultiwii import MultiWii
import time

board = MultiWii("/dev/ttyACM0")

board.prearm()
board.arm()

while True:
    board.sendCMD(16, MultiWii.SET_RAW_RC, [
                  1500, 1500, 1800, 1500, 2000, 1000, 1000, 1000], '8H')
    time.sleep(0.05)
    print(board.getData(MultiWii.MOTOR))
    time.sleep(0.05)
