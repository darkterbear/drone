#!/usr/bin/env python

from msp import MultiWii
from util import push16
import time

board = MultiWii("/dev/ttyACM0")

time.sleep(1.0)

board.arm()
print('Armed')
time.sleep(1.0)

while True:
    buf = []
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1000)
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1000)
    push16(buf, 1000)
    push16(buf, 1000)
    board.sendCMD(MultiWii.SET_RAW_RC, buf)
    time.sleep(0.025)
    print(board.getData(MultiWii.RC))
    time.sleep(0.025)
