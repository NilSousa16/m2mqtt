package br.ufba.dcc.wiser.m2mqtt.service;

public interface IListenerMqttInfoService {
	public void sendInformationServer(String message, String topic);
}
