#!/usr/bin/env python

from pymultiwii import MultiWii
from sys import stdout

if __name__ == "__main__":

    board = MultiWii("/dev/ttyACM0")
    try:
        while True:
            board.getData(MultiWii.ATTITUDE)
            print(board.attitude)
    except Exception as error:
        print("Error on Main: " + str(error))
