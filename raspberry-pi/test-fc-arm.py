#!/usr/bin/env python

from pymultiwii import MultiWii
import time

board = MultiWii("/dev/ttyACM0")

time.sleep(1.0)

board.prearm()

print('Armed')

time.sleep(1.0)

board.sendCMD(MultiWii.SET_MOTOR, [
              2000, 2000, 2000, 2000, 1000, 1000, 1000, 1000])

print('Motors HIGH')

time.sleep(1.0)

print(board.getData(MultiWii.MOTOR))

# while True:
#     board.sendCMD(16, MultiWii.SET_RAW_RC, [
#                   1500, 1500, 1800, 1500, 2000, 1000, 1000, 1000], '8H')
#     time.sleep(0.05)
#     print(board.getData(MultiWii.MOTOR))
#     time.sleep(0.05)
