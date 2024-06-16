/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufba.dcc.wiser.m2mqtt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufba.dcc.wiser.m2mqtt.communication.ListenMQTTMessage;
import br.ufba.dcc.wiser.m2mqtt.communication.MQTTClientDevice;
import br.ufba.dcc.wiser.m2mqtt.impl.DeviceSimulator;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

public class Activator implements BundleActivator {

	MQTTClientDevice clientMQTTCommunication = new MQTTClientDevice(Consts.BROKER_IP, null, null);
	String[] topics = { Consts.REGISTER_DEVICE, Consts.SEND_INFO_DEVICE };

	DeviceSimulator deviceSimulator;

	public void start(BundleContext context) throws InterruptedException {
		clientMQTTCommunication.start();

		deviceSimulator = new DeviceSimulator(clientMQTTCommunication, 10);
		
		new ListenMQTTMessage(clientMQTTCommunication, 0, deviceSimulator, topics);

		// deviceSimulator.deviceMonitor();
		
		// envio das mensagens em background sobre o status dos dispositivos
		// deviceSimulator.deviceMonitor();
		// MONTAR DEVICEMONITOR PARA BACKGROUND
		// deviceSimulator
		
		System.out.println("Starting the bundle - m2mqtt");
	}

	public void stop(BundleContext context) {
		clientMQTTCommunication.finish();
		System.out.println("Stopping the bundle - m2mqtt");
	}

}