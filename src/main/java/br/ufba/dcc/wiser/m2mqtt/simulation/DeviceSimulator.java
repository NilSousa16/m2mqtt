package br.ufba.dcc.wiser.m2mqtt.simulation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2mqtt.communication.MQTTClientDevice;
import br.ufba.dcc.wiser.m2mqtt.service.IDeviceSimulatorMqttInfoService;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;
import br.ufba.dcc.wiser.m2mqtt.utils.DeviceDataGenerator;

public class DeviceSimulator implements IDeviceSimulatorMqttInfoService {
	private List<Device> listDevices;
	private DeviceDataGenerator deviceDataGenerator;

	private MQTTClientDevice clientMQTTCommunication;

	private Gson gson;
	private Random random;

	public DeviceSimulator(MQTTClientDevice clientMQTTCommunication, int quantityDevices) {
		String location, description, typeDevice, category;
		Boolean status;
		Calendar date;

		listDevices = new ArrayList<>();
		deviceDataGenerator = new DeviceDataGenerator();

		this.clientMQTTCommunication = clientMQTTCommunication;

		random = new Random();

		for (int interator = 0; interator < quantityDevices; interator++) {
			location = deviceDataGenerator.selectRandomLocation();
			description = deviceDataGenerator.generateRandomText(150);
			typeDevice = deviceDataGenerator.selectRandomCategory();
			category = random.nextBoolean() ? "Actuator" : "Sensor";
			status = random.nextBoolean();
			date = Calendar.getInstance();

			this.createDevice(location, description, typeDevice, category, status, date, null);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void createDevice(String location, String description, String typeDevice, String category, Boolean status,
			Calendar date, String macGateway) {
		String id = UUID.randomUUID().toString();
		Device device = new Device(id, location, description, typeDevice, category, status, date, macGateway);

		gson = new Gson();
		Object objectDevice = device;
		String jsonObject = gson.toJson(objectDevice);

		clientMQTTCommunication.publish(Consts.REGISTER_DEVICE, jsonObject, 0);

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

			gson = new Gson();
			Object objectDevice = device;
			String jsonObject = gson.toJson(objectDevice);

			clientMQTTCommunication.publish(Consts.SEND_INFO_DEVICE, jsonObject, 0);
		} else {
			System.out.println("Device not found.");
		}
	}

	public void deviceMonitor() {
		this.listDevices.forEach(device -> {
			if (random.nextBoolean()) {
				this.updateDeviceStatus(device.getId(), !device.getStatus());
			}
		});
	}
}
