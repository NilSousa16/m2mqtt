package br.ufba.dcc.wiser.m2mqtt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufba.dcc.wiser.m2mqtt.communication.ListenMQTTMessage;
import br.ufba.dcc.wiser.m2mqtt.communication.MQTTClientDevice;
import br.ufba.dcc.wiser.m2mqtt.services.DeviceStatusUpdater;
import br.ufba.dcc.wiser.m2mqtt.simulation.DeviceSimulator;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

/**
 * Activator class that implements BundleActivator to manage the lifecycle of
 * the OSGi bundle.
 * 
 * @author Nilson Rodrigues Sousa
 */
public class Activator implements BundleActivator {

	/**
	 * MQTT client communication instance to handle MQTT operations.
	 * 
	 * Initialized with the broker IP from Consts and no authentication (null user
	 * and password).
	 */
	MQTTClientDevice clientMQTTCommunication = new MQTTClientDevice(Consts.BROKER_IP, null, null);

	/**
	 * Topics array that includes the topic for receiving device modify settings.
	 * 
	 * Used for subscribing the MQTT client to listen for specific messages.
	 */
	String[] topics = { Consts.RECEIVE_DEVICE_MODIFY_SETTINGS };

	/**
	 * Instance of DeviceStatusUpdater to periodically update the status of devices.
	 * 
	 * This instance runs in a separate thread and periodically updates device
	 * statuses.
	 */
	private DeviceStatusUpdater deviceStatusUpdater;

	/**
	 * DeviceSimulator instance to simulate devices and their interactions.
	 * 
	 * This simulator is used to create, manage, and simulate the behavior of
	 * devices.
	 */
	DeviceSimulator deviceSimulator;

	/**
	 * Called when the bundle is started.
	 *
	 * Initializes the MQTT client, device simulator, message listener, and device
	 * status updater.
	 *
	 * @param context The execution context of the bundle being started
	 * @throws InterruptedException If the thread is interrupted while starting the
	 *                              components
	 */
	public void start(BundleContext context) throws InterruptedException {
		clientMQTTCommunication.start();

		deviceSimulator = new DeviceSimulator(clientMQTTCommunication, 10);

		new ListenMQTTMessage(clientMQTTCommunication, 0, deviceSimulator, topics);

		deviceStatusUpdater = new DeviceStatusUpdater(deviceSimulator);
		deviceStatusUpdater.start();

		System.out.println("Starting the bundle - m2mqtt");
	}

	/**
	 * Called when the bundle is stopped.
	 *
	 * Stops the device status updater and disconnects the MQTT client.
	 *
	 * @param context The execution context of the bundle being stopped
	 */
	public void stop(BundleContext context) {
		if (deviceStatusUpdater != null) {
			deviceStatusUpdater.stop();
		}
		if (clientMQTTCommunication != null) {
			clientMQTTCommunication.finish();
		}

		System.out.println("Stopping the bundle - m2mqtt");
	}

}