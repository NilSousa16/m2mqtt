package br.ufba.dcc.wiser.m2mqtt.communication;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ListenMQTTMessage implements IMqttMessageListener {

	/* instance responsible for sending the information to the server */
	// ServerCommunication serverCommunication = new ServerCommunication();

	private final MQTTClientDevice clientMQTT;

	public ListenMQTTMessage(MQTTClientDevice clientMQTT, String topic, int qos) {
		this.clientMQTT = clientMQTT;
        clientMQTT.subscribe(qos, this, topic);
	}

	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
		System.out.println("Mensagem recebida:");
        System.out.println("\tTópico: " + topic);
        System.out.println("\tMensagem: " + new String(mm.getPayload()));
        System.out.println("");
        
        if(topic.equals("manager/register")) {
        	System.out.println("Executar modificação 01 no dispositivo");
        	new SendMQTTMessage(clientMQTT).publish("test/topic", "Hello MQTT", 2);
        } else if(topic.equals("manager/data")) {
        	System.out.println("Executar modificação 02 no dispositivo");
        }      
	}

}
