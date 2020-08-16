#!/usr/bin/env python

from msp import MultiWii
from util import push16
import time

board = MultiWii("/dev/ttyACM0")

time.sleep(1.0)

board.enable_arm()
board.arm()

while True:
    buf = []
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1500)
    push16(buf, 1000)
    push16(buf, 1000)
    push16(buf, 1000)
    board.sendCMD(MultiWii.SET_RAW_RC, buf)
    time.sleep(0.05)
