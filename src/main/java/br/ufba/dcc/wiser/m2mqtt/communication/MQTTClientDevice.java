package br.ufba.dcc.wiser.m2mqtt.communication;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * This class handles MQTT communication for a device, managing connection,
 * subscription, and message publishing.
 * 
 * @author Nilson Rodrigues Sousa
 */
public class MQTTClientDevice implements MqttCallbackExtended {

	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;
	private final BlockingQueue<Message> messageQueue;
	private final ExecutorService executorService;

	/**
	 * Constructs an MQTTClientDevice with specified server URI and optional
	 * username and password.
	 *
	 * @param serverURI The URI of the MQTT server.
	 * @param user      The username for MQTT authentication (can be null).
	 * @param password  The password for MQTT authentication (can be null).
	 */
	public MQTTClientDevice(String serverURI, String user, String password) {
		this.serverURI = serverURI;

		mqttOptions = new MqttConnectOptions();
		mqttOptions.setMaxInflight(200);
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);

		if (user != null && password != null) {
			mqttOptions.setUserName(user);
			mqttOptions.setPassword(password.toCharArray());
		}

		messageQueue = new LinkedBlockingQueue<>();
		executorService = Executors.newSingleThreadExecutor();
		startMessagePublisher();
	}

	/**
	 * Starts the message publisher which continuously takes messages from the queue
	 * and publishes them.
	 */
	private void startMessagePublisher() {
		executorService.execute(() -> {
			try {
				while (true) {
					Message message = messageQueue.take();
					if (client.isConnected()) {
						client.publish(message.getTopic(), message.getMqttMessage());
						System.out.println("Message published in " + message.getTopic());
					} else {
						System.out.println("Client disconnected, topic could not be published " + message.getTopic());
					}
				}
			} catch (InterruptedException | MqttException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Subscribes to the specified topics with a given quality of service (QoS).
	 *
	 * @param qos                The quality of service level.
	 * @param managerMessageMQTT The message listener to handle incoming messages.
	 * @param topics             The topics to subscribe to.
	 * @return The MQTT subscription token.
	 */
	public IMqttToken subscribe(int qos, IMqttMessageListener managerMessageMQTT, String... topics) {
		if (client == null || topics.length == 0) {
			return null;
		}
		int tamanho = topics.length;
		int[] qoss = new int[tamanho];
		IMqttMessageListener[] listners = new IMqttMessageListener[tamanho];

		for (int i = 0; i < tamanho; i++) {
			qoss[i] = qos;
			listners[i] = managerMessageMQTT;
		}
		try {
			return client.subscribeWithResponse(topics, qoss, listners);
		} catch (MqttException ex) {
			System.out.println(String.format("Error subscribing to topics %s - %s", Arrays.asList(topics), ex));
			return null;
		}
	}

	/**
	 * Unsubscribes from the specified topics.
	 *
	 * @param topics The topics to unsubscribe from.
	 */
	public void unsubscribe(String... topics) {
		if (client == null || !client.isConnected() || topics.length == 0) {
			return;
		}
		try {
			client.unsubscribe(topics);
		} catch (MqttException ex) {
			System.out.println(String.format("Error when unsubscribing to topic %s - %s", Arrays.asList(topics), ex));
		}
	}

	/**
	 * Connects to the MQTT server and starts the client.
	 */
	public void start() {
		try {
			System.out.println("Connecting to the MQTT broker in " + serverURI);
			client = new MqttClient(serverURI, String.format("cliente_java_%d", System.currentTimeMillis()),
					new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			client.setCallback(this);
			client.connect(mqttOptions);
		} catch (MqttException ex) {
			System.out.println("Error connecting to mqtt broker " + serverURI + " - " + ex);
		}
	}

	/**
	 * Disconnects from the MQTT server and stops the client.
	 */
	public void finish() {
		if (client == null || !client.isConnected()) {
			return;
		}
		try {
			client.disconnect();
			client.close();
		} catch (MqttException ex) {
			System.out.println("Error disconnecting from mqtt broker - " + ex);
		}
	}

	/**
	 * Publishes a message to the specified topic with a given quality of service
	 * (QoS).
	 *
	 * @param topic   The topic to publish to.
	 * @param content The message content.
	 * @param qos     The quality of service level.
	 */
	public void publish(String topic, String content, int qos) {
		try {
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			messageQueue.put(new Message(topic, message));
			System.out.println("Message queued for topic: " + topic);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println("Failed to queue message for topic: " + topic + " - " + e);
		}
	}

	/**
	 * Handles connection loss with the MQTT server.
	 *
	 * @param thrwbl The cause of the connection loss.
	 */
	@Override
	public void connectionLost(Throwable thrwbl) {
		System.out.println("Broker connection lost -" + thrwbl);
	}

	/**
	 * Called when the connection to the MQTT server is complete.
	 *
	 * @param reconnect Indicates if the connection is a reconnection.
	 * @param serverURI The URI of the MQTT server.
	 */
	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("Cliente MQTT " + (reconnect ? "reconnecting" : "connecting") + " with the broker " + serverURI);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken imdt) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage mm) throws Exception {
	}

	/**
	 * Represents a message to be published to an MQTT topic.
	 */
	private static class Message {
		private final String topic;
		private final MqttMessage mqttMessage;

		/**
		 * Constructs a Message with the specified topic and MQTT message.
		 *
		 * @param topic       The topic to publish to.
		 * @param mqttMessage The MQTT message content.
		 */
		public Message(String topic, MqttMessage mqttMessage) {
			this.topic = topic;
			this.mqttMessage = mqttMessage;
		}

		/**
		 * Gets the topic of the message.
		 *
		 * @return The topic.
		 */
		public String getTopic() {
			return topic;
		}

		/**
		 * Gets the MQTT message content.
		 *
		 * @return The MQTT message content.
		 */
		public MqttMessage getMqttMessage() {
			return mqttMessage;
		}
	}

}
