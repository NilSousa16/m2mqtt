package br.ufba.dcc.wiser.m2mqtt.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2mqtt.service.IDeviceSimulatorMqttInfoService;
import br.ufba.dcc.wiser.m2mqtt.utils.DeviceDataGenerator;

public class DeviceSimulator implements IDeviceSimulatorMqttInfoService {
	private List<Device> listDevices;
	private DeviceDataGenerator deviceDataGenerator;

	private String location;
	private String description;
	private String typeDevice;
	private String category;
	private Boolean status;
	private Calendar date;
	
	public DeviceSimulator(int quantityDevices) {
		listDevices = new ArrayList<>();
		deviceDataGenerator = new DeviceDataGenerator();
		Random random = new Random();

		for (int interator = 0; interator < quantityDevices; interator++) {
			location = deviceDataGenerator.selectRandomLocation();
			description = deviceDataGenerator.generateRandomText(150);
			typeDevice = deviceDataGenerator.selectRandomCategory();
			category = random.nextBoolean() ? "Actuator" : "Sensor";
			status = random.nextBoolean();
			date = Calendar.getInstance();

			this.createDevice(location, description, typeDevice, category, status, date, null);
		}
	}

	public void createDevice(String location, String description, String typeDevice, String category, Boolean status,
			Calendar date, String macGateway) {
		String id = UUID.randomUUID().toString();
		Device device = new Device(id, location, description, typeDevice, category, status, date, macGateway);
		listDevices.add(device);
	}

	public List<Device> getListDevices() {
		return listDevices;
	}

	public Device getDeviceById(String id) {
		for (Device device : listDevices) {
			if (device.getId() == id) {
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
