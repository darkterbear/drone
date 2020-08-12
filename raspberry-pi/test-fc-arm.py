#!/usr/bin/env python

from pymultiwii import MultiWii
import time

board = MultiWii("/dev/ttyACM0")
board.arm()

time.sleep(1.0)
board.disarm()
