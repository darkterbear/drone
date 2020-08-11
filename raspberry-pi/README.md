# Raspberry Pi

This subdirectory involves the code run on the Raspberry Pi to communicate with the flight controller.

## Prerequisites
- `python3`
- `pip3`

## Installation
- `pip3 install pyserial`
- `pip3 install bluedot`
- `pip3 install .`

## test-fc.py
This script tests the serial connection between the RPi and the flight controller. If successful, this will output the FC attitude continuously.

1. Connect MSP/Betaflight flight controller to RPi or computer using USB or other serial port
2. Find the serial port that the flight controller is on (e.g. COM5, /dev/ttyUSB0, etc.)
3. In test-fc.py, change the board declaration to use the serial port name: `board = MultiWii("[SERIAL PORT NAME HERE]")`
4. Run test-fc.py for attitude output

## test-bluetooth.py
This script tests the Bluetooth connection between the RPi and the Android controller. If successful, this will output the RC commands sent by the Android controller.

1. Pair the RPi with the Android phone via Bluetooth
2. Run test-bluetooth.py
3. Open the Controller app
4. Select the Raspberry Pi in the app to initiate a connection
5. Once the dual d-pads appear, perform movements and observe commands outputted
