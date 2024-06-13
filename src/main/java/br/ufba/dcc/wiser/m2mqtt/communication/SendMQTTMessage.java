package br.ufba.dcc.wiser.m2mqtt.communication;

public class SendMQTTMessage {

	private final MQTTClientDevice clienteMQTT;

	public SendMQTTMessage(MQTTClientDevice mqttClient) {
		this.clienteMQTT = mqttClient;
	}

	public void publish(String topic, String content, int qos) {
		clienteMQTT.publish(topic, content, 2);
	}
}
