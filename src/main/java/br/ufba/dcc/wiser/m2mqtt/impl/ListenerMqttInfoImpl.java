package br.ufba.dcc.wiser.m2mqtt.impl;

import java.util.Calendar;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2client.impl.GatewayHardwareInfoService;
import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2model.model.DeviceStatusTransfer;
import br.ufba.dcc.wiser.m2model.model.Gateway;
import br.ufba.dcc.wiser.m2mqtt.communication.ClientMQTTCommunication;
import br.ufba.dcc.wiser.m2mqtt.communication.ServerCommunication;
import br.ufba.dcc.wiser.m2mqtt.service.IListenerMqttInfoService;

public class ListenerMqttInfoImpl implements IMqttMessageListener, IListenerMqttInfoService {

	/* instance responsible for sending the information to the server */
	// ServerCommunication serverCommunication = new ServerCommunication();

	public ListenerMqttInfoImpl(ClientMQTTCommunication clientMQTTCommunication, String topico, int qos) {
		clientMQTTCommunication.subscribe(qos, this, topico);
		System.out.println("Subscribed to topic: " + topico);
	}

	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {

		String message = new String(mm.getPayload());

		this.sendInformationServer(message, topic);
	}

	@Override
	public void sendInformationServer(final String message, final String topic) {
		/*
		 * This method is necessary so that we can start() the Thread and 
		 * start the task in parallel
		 */
		new Thread() {
			// register - Checks if the device has already been registered
			Boolean register = false;

			public void run() {
				ServerCommunication serverCommunication = new ServerCommunication();
				GatewayHardwareInfoService gatewayHardwareInfoService = new GatewayHardwareInfoService();

				if (topic.equals("manager/register") && !register) {
					Gson gson = new Gson();
					Device device = new Device();

					device = gson.fromJson(message, Device.class);

					/* Retrieved gateway information */
					// fetch from gateway
					device.setLocation("12.9922935,-38.5166708"); 
					/* Recording the moment of data capture */
					device.setDate(Calendar.getInstance());
					/*
					 * Gateway ID linked to the device Retrieved gateway information
					 */
					device.setGateway(
							new Gateway(gatewayHardwareInfoService.getMacAddress(), null, null, null, true, null, null,  null));

					try {
						if (serverCommunication.send(device)) {
							System.out.println("M2MQTT - Submission completed successfully");
							register = true;
						} else {
							System.out.println("M2MQTT - Submission was not completed successfully");
						}
					} catch (Exception e) {
						System.out.println("M2MQTT - The data could not be sent");
						// e.printStackTrace();
					}
				} else { // topic == manager/data
					Gson gson = new Gson();
					// information sent refers to the status data of the devices
					DeviceStatusTransfer deviceStatusTransfer = new DeviceStatusTransfer();
					deviceStatusTransfer = gson.fromJson(message, DeviceStatusTransfer.class);

					// generating information
					deviceStatusTransfer.setDate(Calendar.getInstance());

					try {
						if (serverCommunication.send(deviceStatusTransfer)) {
							System.out.println("M2MQTT - Status information has been sent");
						} else {
							System.out.println("M2MQTT - Status information was not sent");
						}
					} catch (Exception e) {
						System.out.println("M2MQTT - Error sending status information from device");
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
