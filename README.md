# drone

Drone project in collaboration with @nmadev.

## Goals
- [ ] Build a functioning drone controlled by a Raspberry Pi flight computer
- [ ] Control the drone over Bluetooth with a PC and/or Android client
- [ ] Fly the drone autonomously over predefined flight plans
- [ ] Autonomously land the drone using computer vision

More to come...

## Hardware
This section will update as we add more features to our drone.
- FC + ESC Stack: Mamba F405 Mk. 2 FC + Mamba F40 Mk. 2 ESC
- Flight Computer: Raspberry Pi 3 B
- Frame: 3D printed according to CAD files
- Motors: iFlight XING-E 2207 1800KV 6S
- Battery: Ovonic 14.8V 1300mAh 100C 4S LiPo
- Battery Monitor: Readytosky LiPo Battery Voltage Tester & Low Voltage Buzzer
- Props: Gemfan Hurricane 51466 (5.1x4.66x3)


## System Schematic
![System Schematic](/images/system_schematic.png)

## Betaflight FC Setup
This section is **very important**, and must be completed before running main.py or test-fc-command.py. This involves going to Betaflight Configurator and configuring the flight controller for use.

1. Download and install [Betaflight Configurator](https://github.com/betaflight/betaflight-configurator/releases) (on a system that is not your RPi, ideally).
2. Plug in the FC to that system, start Betaflight Configurator, select the appropriate serial port and hit "Connect"
3. In the left panel, go to "Configuration". 
4. In the "Receiver" section, select "MSP RX input (control via MSP port)". Click "Save and Reboot" and wait momentarily.
5. In the left panel, go to "Modes". 
6. In the "ARM" mode, click "Add Range", if there is no range already. 
7. In that range, select "AUX 1" as the channel, and 1400-1600 as the range. Click "Save".
