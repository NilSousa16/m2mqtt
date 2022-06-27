package br.ufba.dcc.wiser.m2mqtt.experiments;

import java.text.SimpleDateFormat;

public class Main {

	public static void main(String[] args) throws InterruptedException {
        ClienteMQTT clienteMQTT = new ClienteMQTT("tcp://192.168.1.105:1883", null, null);
        clienteMQTT.iniciar();

        new Ouvinte(clienteMQTT, "manager/register", 0);
        new Ouvinte(clienteMQTT, "manager/data", 0);
        /*
        while (true) {
            Thread.sleep(1000);
            String mensagem = "Mensagem enviada em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis()) + " - Teste de MQTT disponivel em www.paulocollares.com.br";

            clienteMQTT.publicar("test", mensagem.getBytes(), 0);
        }
		*/
    }

}
