package br.ufba.dcc.wiser.m2mqtt.communication;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2model.dto.DeviceStatusDTO;
import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2mqtt.simulation.DeviceSimulator;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

/**
 * This class listens for MQTT messages and processes them based on the
 * specified topics.
 *
 * @author Nilson Rodrigues Sousa
 */
public class ListenMQTTMessage implements IMqttMessageListener {

	private MQTTClientDevice clientMQTTCommunication;

	private DeviceSimulator deviceSimulator;

	/**
	 * Constructs a ListenMQTTMessage with specified MQTT client, QoS level, device
	 * simulator, and topics.
	 *
	 * @param clientMQTTCommunication The MQTT client for communication.
	 * @param qos                     The quality of service level.
	 * @param deviceSimulator         The simulator to update or retrieve device
	 *                                status.
	 * @param topics                  The topics to subscribe to.
	 */
	public ListenMQTTMessage(MQTTClientDevice clientMQTTCommunication, int qos, DeviceSimulator deviceSimulator,
			String... topics) {

		this.clientMQTTCommunication = clientMQTTCommunication;
		this.deviceSimulator = deviceSimulator;

		clientMQTTCommunication.subscribe(qos, this, topics);
	}

	/**
	 * Handles the arrival of an MQTT message by processing it based on the topic.
	 *
	 * @param topic The topic the message was received on.
	 * @param mm    The received MQTT message.
	 * @throws Exception if an error occurs while processing the message.
	 */
	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
		System.out.println("Mensagem recebida:");
		System.out.println("\tTópico: " + topic);
		System.out.println("\tMensagem: " + new String(mm.getPayload()));
		System.out.println("");

		Gson gson = new Gson();
		String jsonPayload = new String(mm.getPayload());

		if (topic.equals(Consts.RECEIVE_DEVICE_MODIFY_SETTINGS)) {
			System.out.println("Topic responsible for RECEIVING device settings");

			DeviceStatusDTO deviceStatusDTO = gson.fromJson(jsonPayload, DeviceStatusDTO.class);
			System.err.println(">>> Conversão do json para deviceStatusDTO concluído...");
			deviceSimulator.updateDeviceStatus(deviceStatusDTO.getIdDevice(), deviceStatusDTO.getStatus());
			System.out.println(">>> Modificação realizada conforme solicitado...");
		} else if (topic.equals(Consts.RECEIVE_DEVICE_REQUEST)) {
			System.out.println("Topic responsible for SENDING data about the device when requested");

			String idDevice = gson.fromJson(jsonPayload, String.class);

			Device deviceFound = deviceSimulator.getDeviceById(idDevice);

			gson = new Gson();
			Object objectDevice = deviceFound;
			String jsonObject = gson.toJson(objectDevice);

			clientMQTTCommunication.publish(Consts.SEND_DEVICE_SETTINGS, jsonObject, 2);
		}
	}

}
