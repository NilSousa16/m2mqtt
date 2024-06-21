# M2 MQTT

[![N|Solid](https://wiser.ufba.br/images/wiser.png)](https://wiser.ufba.br/)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

This bundle is responsible for sending data from devices that communicate via mqtt to the gateway

## Topics in use

The topics listed below are used to facilitate communication between the device and the gateway, as well as the interaction between the gateway and the device.

- **manager/register**: topic responsible for SENDing data for device registration
- **manager/data**: topic responsible for SENDING device management data constantly

- **manager/device/settings/modify**: topic responsible for RECEIVING device settings
- **manager/device/settings/request**: topic responsible for SEND data about the device when requested
