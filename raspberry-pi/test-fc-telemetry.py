#!/usr/bin/env python

from msp import MultiWii

board = MultiWii("/dev/ttyACM0")
while True:
    board.getData(MultiWii.ATTITUDE)
    print(board.attitude)
