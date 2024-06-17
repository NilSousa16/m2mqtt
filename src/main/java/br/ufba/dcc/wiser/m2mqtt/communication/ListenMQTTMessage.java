package br.ufba.dcc.wiser.m2mqtt.communication;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.ufba.dcc.wiser.m2mqtt.simulation.DeviceSimulator;

/**
 *  manager/register - tópico responsável por ENVIAR dados para o cadastro do dispositivo
		 manager/data - tópico responsável por ENVIAR dados gerenciais do dispositivo
		 constantemente
		 manager/data/settings/device - tópico responsável por RECEBER configurações
		 para dispositivo
		 manager/data/settings/status - tópico responsável por ENVIAR dados sobre o
		 dispositivo quando solicitado
 */
public class ListenMQTTMessage implements IMqttMessageListener {

	private MQTTClientDevice clientMQTTCommunication;
	private DeviceSimulator deviceSimulator;

	public ListenMQTTMessage(MQTTClientDevice clientMQTTCommunication, int qos, DeviceSimulator deviceSimulator, String... topics) {
		
		this.clientMQTTCommunication = clientMQTTCommunication;
		this.deviceSimulator = deviceSimulator;	

		clientMQTTCommunication.subscribe(qos, this, topics);
	}

	/**
	 * 
	 */
	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
		System.out.println("Mensagem recebida:");
		System.out.println("\tTópico: " + topic);
		System.out.println("\tMensagem: " + new String(mm.getPayload()));
		System.out.println("");

		if (topic.equals("manager/register")) {
			System.out.println("Executar modificação 01 no dispositivo");
			clientMQTTCommunication.publish("test/topic", "Hello MQTT", 2);
		} else if (topic.equals("manager/data")) {
			clientMQTTCommunication.publish("test/topic", "Hello MQTT 2", 2);
			System.out.println("Executar modificação 02 no dispositivo");
		}
	}

}
