#!/usr/bin/env python

from msp import MultiWii
from util import push16
import time

board = MultiWii("/dev/ttyACM0")

time.sleep(1.0)

board.arm()
print('Armed')
time.sleep(1.0)

buf = []
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
push16(buf, 2000)
board.sendCMD(MultiWii.SET_MOTOR, buf)

print('Motors HIGH')
time.sleep(5.0)

buf = []
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
push16(buf, 1500)
board.sendCMD(MultiWii.SET_MOTOR, buf)

print('Motors MEDIUM')
time.sleep(5.0)

board.disarm()
print('Disarmed')
time.sleep(1.0)

# while True:
#     board.sendCMD(16, MultiWii.SET_RAW_RC, [
#                   1500, 1500, 1800, 1500, 2000, 1000, 1000, 1000], '8H')
#     time.sleep(0.05)
#     print(board.getData(MultiWii.MOTOR))
#     time.sleep(0.05)
