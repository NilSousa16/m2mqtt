package br.ufba.dcc.wiser.m2mqtt.service;

import java.util.Calendar;
import java.util.List;

import br.ufba.dcc.wiser.m2model.model.Device;

public interface IDeviceSimulatorMqttInfoService {

	public void createDevice(String location, String description, String typeDevice, String category, Boolean status,
			Calendar date, String macGateway);

	public List<Device> getListDevices();

	public Device getDeviceById(String id);

	public void updateDeviceStatus(String id, Boolean status);

}
