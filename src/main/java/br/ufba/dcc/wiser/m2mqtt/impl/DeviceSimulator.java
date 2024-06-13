package br.ufba.dcc.wiser.m2mqtt.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2mqtt.service.IDeviceSimulatorMqttInfoService;

public class DeviceSimulator implements IDeviceSimulatorMqttInfoService {
	private List<Device> listDevices;

	public DeviceSimulator() {
		listDevices = new ArrayList<>();
	}

	public void createDevice(String location, String description, String typeDevice, String category, Boolean status,
			Calendar date, String macGateway ) {
		String id = UUID.randomUUID().toString();
		Device device = new Device(id, location, description, typeDevice, category, status, date, macGateway);
		listDevices.add(device);
	}

	public List<Device> getListDevices() {
		return listDevices;
	}

	public Device getDeviceById(String id) {
		for (Device device : listDevices) {
			if(device.getId() == id) {
				return device;
			}            
        }
		
		return null;
	}
	
	public void updateDeviceStatus(String id, Boolean status) {
        Device device = getDeviceById(id);
        if (device != null) {
            device.setStatus(status);
        }
    }
}
