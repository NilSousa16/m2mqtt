package br.ufba.dcc.wiser.m2mqtt.communication;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MQTTClientDevice implements MqttCallbackExtended {

	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;

	public MQTTClientDevice(String serverURI, String usuario, String senha) {
		this.serverURI = serverURI;

		mqttOptions = new MqttConnectOptions();
		mqttOptions.setMaxInflight(200);
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);

		if (usuario != null && senha != null) {
			mqttOptions.setUserName(usuario);
			mqttOptions.setPassword(senha.toCharArray());
		}
	}

	//
	public IMqttToken subscribe(int qos, IMqttMessageListener gestorMensagemMQTT, String... topicos) {
		if (client == null || topicos.length == 0) {
			return null;
		}
		int tamanho = topicos.length;
		int[] qoss = new int[tamanho];
		IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];

		for (int i = 0; i < tamanho; i++) {
			qoss[i] = qos;
			listners[i] = gestorMensagemMQTT;
		}
		try {
			return client.subscribeWithResponse(topicos, qoss, listners);
		} catch (MqttException ex) {
			System.out.println(String.format("Erro ao se inscrever nos tópicos %s - %s", Arrays.asList(topicos), ex));
			return null;
		}
	}

	public void unsubscribe(String... topicos) {
		if (client == null || !client.isConnected() || topicos.length == 0) {
			return;
		}
		try {
			client.unsubscribe(topicos);
		} catch (MqttException ex) {
			System.out.println(String.format("Erro ao se desinscrever no tópico %s - %s", Arrays.asList(topicos), ex));
		}
	}

	public void start() {
		try {
			System.out.println("Conectando no broker MQTT em " + serverURI);
			client = new MqttClient(serverURI, String.format("cliente_java_%d", System.currentTimeMillis()),
					new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			client.setCallback(this);
			client.connect(mqttOptions);
		} catch (MqttException ex) {
			System.out.println("Erro ao se conectar ao broker mqtt " + serverURI + " - " + ex);
		}
	}

	public void finish() {
		if (client == null || !client.isConnected()) {
			return;
		}
		try {
			client.disconnect();
			client.close();
		} catch (MqttException ex) {
			System.out.println("Erro ao desconectar do broker mqtt - " + ex);
		}
	}

	public void publish(String topic, String content, int qos) {
		try {
			if (client.isConnected()) {
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				client.publish(topic, message);
				System.out.println("Message published in " + topic);
			} else {
				System.out.println("Client disconnected, topic could not be published " + topic);
			}
		} catch (MqttException ex) {
			System.out.println("Error publishing " + topic + " - " + ex);
		}

	}

	@Override
	public void connectionLost(Throwable thrwbl) {
		System.out.println("Conexão com o broker perdida -" + thrwbl);
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("Cliente MQTT " + (reconnect ? "reconectado" : "conectado") + " com o broker " + serverURI);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken imdt) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
	}

}
