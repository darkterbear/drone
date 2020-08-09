# Raspberry Pi

This subdirectory involves the code run on the Raspberry Pi to communicate with the flight controller.

## Prerequisites
- `python3`
- `pip3`

## Installation
- `pip3 install pyserial`
- `pip3 install .`

## Usage
1. Connect MSP/Betaflight flight controller to RPi or computer using USB or other serial port
2. Find the serial port that the flight controller is on (e.g. COM5, /dev/ttyUSB0, etc.)
3. In the main script (test.py), change the board declaration to use the serial port name: `board = MultiWii("[SERIAL PORT NAME HERE]")`
4. Run test.py for attitude output
