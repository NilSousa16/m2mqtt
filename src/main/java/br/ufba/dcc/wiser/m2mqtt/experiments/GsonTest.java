package br.ufba.dcc.wiser.m2mqtt.experiments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2model.model.DeviceStatus;
import br.ufba.dcc.wiser.m2model.model.Gateway;

public class GsonTest {

	public static void main(String[] args) {
		
		SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		int op = 5;
		
		if (op == 1) {
			Gson gson = new Gson();
			Device device = new Device();

			device.setId("156512d");
			device.setLocation("12.9922935,-38.5166708");
			device.setDescription("Device monitor temperature");
			device.setTypeSensor("Temperature");
			device.setStatus(true);
			device.setDate(Calendar.getInstance());
			device.setGateway(new Gateway("88.55.88.55.88.55", "192.168.1.0", null, null, false, null, null, null));

			String jsonObject = gson.toJson(device);

			System.out.println(jsonObject);

			System.out.println("----------------------");

			Device deviceConvertido = gson.fromJson(jsonObject, Device.class);
			
			System.out.println("Id: " + deviceConvertido.getId());
			System.out.println("Location: " + deviceConvertido.getLocation());
			System.out.println("Description: " + deviceConvertido.getDescription());
			System.out.println("Type Sensor: " + deviceConvertido.getTypeSensor());
			System.out.println("Status: " + deviceConvertido.getStatus());
			System.out.println("Date: " + form.format(deviceConvertido.getDate().getTime()));
			System.out.println("Gateway: " + deviceConvertido.getGateway().getMac());
		} if (op == 2) {
			Gson gson = new Gson();
			DeviceStatus deviceStatus = new DeviceStatus();

			// generating information
			deviceStatus.setDevice(new Device("12.9922935,-38.5166708", "Device monitor temperature", "Temperature", true, Calendar.getInstance(), "88.55.88.55.88.55"));
			deviceStatus.setDate(Calendar.getInstance());
			deviceStatus.setSituation("operational");
			
			String jsonObject = gson.toJson(deviceStatus);

			System.out.println(jsonObject);
			
			System.out.println("----------------------");

			DeviceStatus deviceStatusConverter = gson.fromJson(jsonObject, DeviceStatus.class);
			
			System.out.println("Device: " + deviceStatusConverter.getDevice());
			System.out.println("Situation: " + deviceStatusConverter.getSituation());
			System.out.println("Date: " + form.format(deviceStatusConverter.getDate().getTime()));
			
		} if (op == 3) {
			UUID uuid = UUID.randomUUID();
	        String uuidAsString = uuid.toString();

	        System.out.println("Your UUID is: " + uuidAsString);
		} if (op == 4) {
			Gson gson = new Gson();
			DeviceStatusTransfer deviceStatusTransfer = new DeviceStatusTransfer();

			// generating information
			deviceStatusTransfer.setIdDevice("54ebdfd4-318a-45b7-93cb-20744782777a");
			deviceStatusTransfer.setDate(Calendar.getInstance());
			deviceStatusTransfer.setSituation("operational");
			
			String jsonObject = gson.toJson(deviceStatusTransfer);

			System.out.println(jsonObject);
			
			System.out.println("----------------------");

			DeviceStatusTransfer deviceStatusTransferConverter = gson.fromJson(jsonObject, DeviceStatusTransfer.class);
			
			System.out.println("Device: " + deviceStatusTransferConverter.getIdDevice());
			System.out.println("Situation: " + deviceStatusTransferConverter.getSituation());
			System.out.println("Date: " + form.format(deviceStatusTransferConverter.getDate().getTime()));
		} if (op == 5) {
			Gson gson;
			
			
		}
	}
}
