#!/usr/bin/env python

from pymultiwii import MultiWii
from sys import stdout

if __name__ == "__main__":

    #board = MultiWii("/dev/ttyUSB0")
    board = MultiWii("/dev/tty.SLAB_USBtoUART")
    try:
        while True:
            board.getData(MultiWii.ATTITUDE)
            # print (board.attitude) #uncomment for regular printing

            # Fancy printing (might not work on windows...)
            message = "angx = {:+.2f} \t angy = {:+.2f} \t heading = {:+.2f} \t elapsed = {:+.4f} \t".format(float(
                board.attitude['angx']), float(board.attitude['angy']), float(board.attitude['heading']), float(board.attitude['elapsed']))
            stdout.write("\r%s" % message)
            stdout.flush()
            # End of fancy printing
    except Exception as error:
        print("Error on Main: "+str(error))
