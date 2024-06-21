package br.ufba.dcc.wiser.m2mqtt.simulation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2mqtt.communication.MQTTClientDevice;
import br.ufba.dcc.wiser.m2mqtt.interfaces.IDeviceSimulatorMqttInfoService;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;
import br.ufba.dcc.wiser.m2mqtt.utils.DeviceDataGenerator;

/**
 * The DeviceSimulator class simulates the creation and management of IoT
 * devices, providing functionalities to create devices, update their statuses,
 * and monitor them.
 * 
 * @author Nilson Rodrigues Sousa
 */

public class DeviceSimulator implements IDeviceSimulatorMqttInfoService {
	private List<Device> listDevices;
	private DeviceDataGenerator deviceDataGenerator;

	private MQTTClientDevice clientMQTTCommunication;

	private Gson gson;
	private Random random;

	/**
	 * Constructs a DeviceSimulator with the specified MQTT client communication and
	 * number of devices.
	 *
	 * @param clientMQTTCommunication The MQTT client communication used for
	 *                                publishing device information.
	 * @param quantityDevices         The number of devices to simulate.
	 */
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

	/**
	 * Creates a new device with the specified parameters and publishes its
	 * information via MQTT.
	 *
	 * @param location    The location of the device.
	 * @param description A description of the device.
	 * @param typeDevice  The type of the device.
	 * @param category    The category of the device (Actuator or Sensor).
	 * @param status      The initial status of the device (true for on, false for
	 *                    off).
	 * @param date        The date of creation of the device.
	 * @param macGateway  The MAC address of the gateway (optional).
	 */
	public void createDevice(String location, String description, String typeDevice, String category, Boolean status,
			Calendar date, String macGateway) {
		String id = UUID.randomUUID().toString();
		Device device = new Device(id, location, description, typeDevice, category, status, date, macGateway);

		gson = new Gson();
		Object objectDevice = device;
		String jsonObject = gson.toJson(objectDevice);

		clientMQTTCommunication.publish(Consts.SEND_DEVICE_REGISTER, jsonObject, 0);

		listDevices.add(device);
	}

	/**
	 * Retrieves the list of all simulated devices.
	 *
	 * @return A list of Device objects representing all simulated devices.
	 */
	public List<Device> getListDevices() {
		return listDevices;
	}

	/**
	 * Retrieves a device by its ID.
	 *
	 * @param id The ID of the device to retrieve.
	 * @return The Device object with the specified ID, or null if no such device
	 *         exists.
	 */
	public Device getDeviceById(String id) {
		for (Device device : listDevices) {
			if (device.getId().equals(id)) {
				return device;
			}
		}
		return null;
	}

	/**
	 * Updates the status of a device with the specified ID and publishes the
	 * updated information via MQTT.
	 *
	 * @param id     The ID of the device to update.
	 * @param status The new status of the device (true for on, false for off).
	 */
	public void updateDeviceStatus(String id, Boolean status) {
		Device device = getDeviceById(id);

		if (device != null) {
			device.setStatus(status);

			gson = new Gson();
			Object objectDevice = device;
			String jsonObject = gson.toJson(objectDevice);

			clientMQTTCommunication.publish(Consts.SEND_DEVICE_INFO, jsonObject, 0);
		} else {
			System.out.println("Device not found.");
		}
	}

	/**
	 * Monitors the status of all simulated devices and randomly toggles their
	 * statuses.
	 */
	public void deviceMonitor() {
		this.listDevices.forEach(device -> {
			if (random.nextBoolean()) {
				this.updateDeviceStatus(device.getId(), !device.getStatus());
			}
		});
	}
}
