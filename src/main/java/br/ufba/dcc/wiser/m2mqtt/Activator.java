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

import br.ufba.dcc.wiser.m2mqtt.communication.ClientMQTTCommunication;
import br.ufba.dcc.wiser.m2mqtt.impl.ListenerMqttInfoImpl;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

public class Activator implements BundleActivator {

	ClientMQTTCommunication clientMQTTCommunication = new ClientMQTTCommunication(Consts.BROKER_IP, null, null);
	
    public void start(BundleContext context) throws InterruptedException {       
    	System.out.println("Starting the bundle - m2mqtt");  
        
        clientMQTTCommunication.start();
        new ListenerMqttInfoImpl(clientMQTTCommunication, "manager/register", 0); 
        new ListenerMqttInfoImpl(clientMQTTCommunication, "manager/data", 0); 
        
        // envio mensagem para o servidor
    }

    public void stop(BundleContext context) {
    	clientMQTTCommunication.finish();
        System.out.println("Stopping the bundle - m2mqtt");
    }

}