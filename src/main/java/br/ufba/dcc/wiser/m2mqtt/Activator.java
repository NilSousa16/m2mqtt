package br.ufba.dcc.wiser.m2mqtt;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufba.dcc.wiser.m2mqtt.communication.ListenMQTTMessage;
import br.ufba.dcc.wiser.m2mqtt.communication.MQTTClientDevice;
import br.ufba.dcc.wiser.m2mqtt.impl.DeviceSimulator;
import br.ufba.dcc.wiser.m2mqtt.impl.DeviceStatusUpdater;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

public class Activator implements BundleActivator {

	MQTTClientDevice clientMQTTCommunication = new MQTTClientDevice(Consts.BROKER_IP, null, null);
	String[] topics = { Consts.MESSAGE_RECEIVED };
	
	private DeviceStatusUpdater deviceStatusUpdater;

	DeviceSimulator deviceSimulator;
	ScheduledExecutorService scheduler;
	Random random;

	public void start(BundleContext context) throws InterruptedException {
		clientMQTTCommunication.start();

		deviceSimulator = new DeviceSimulator(clientMQTTCommunication, 10);
		
		new ListenMQTTMessage(clientMQTTCommunication, 0, deviceSimulator, topics);

		deviceStatusUpdater = new DeviceStatusUpdater(deviceSimulator);
        deviceStatusUpdater.start();
		
		System.out.println("Starting the bundle - m2mqtt");
	}

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