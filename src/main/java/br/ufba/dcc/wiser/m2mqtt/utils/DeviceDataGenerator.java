package br.ufba.dcc.wiser.m2mqtt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceDataGenerator {
	private List<String> listSimulationLocation;
	private List<String> listSimulationCategory;
	
	public DeviceDataGenerator() {
		this.listSimulationLocation = new ArrayList<>();
        this.listSimulationCategory = new ArrayList<>();
        
        // Generate distinct coordinates
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            double latitude = -90 + (90 - (-90)) * random.nextDouble();
            double longitude = -180 + (180 - (-180)) * random.nextDouble();
            String coordinate = String.format("%.4f, %.4f", latitude, longitude);
            listSimulationLocation.add(coordinate);
        }

        listSimulationCategory.add("Sensor de temperatura");
        listSimulationCategory.add("Sensor de umidade");
        listSimulationCategory.add("Sensor de pressão atmosférica");
        listSimulationCategory.add("Sensor de qualidade do ar");
        listSimulationCategory.add("Sensor de luz");
        listSimulationCategory.add("Sensor PIR (infravermelho passivo)");
        listSimulationCategory.add("Acelerômetro");
        listSimulationCategory.add("Giroscópio");
        listSimulationCategory.add("Sensor ultrassônico");
        listSimulationCategory.add("Sensor de proximidade infravermelho");
        listSimulationCategory.add("Sensor de proximidade capacitivo");
        listSimulationCategory.add("Sensor de fluxo de água");
        listSimulationCategory.add("Sensor de fluxo de ar");
        listSimulationCategory.add("Sensor de nível de líquido");
        listSimulationCategory.add("Sensor de gás metano (CH4)");
        listSimulationCategory.add("Sensor de monóxido de carbono (CO)");
        listSimulationCategory.add("Sensor de dióxido de carbono (CO2)");
        listSimulationCategory.add("Sensor de frequência cardíaca");
        listSimulationCategory.add("Sensor de impressão digital");
	}
	
	public String selectRandomLocation () {        
        if (!listSimulationLocation.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(listSimulationLocation.size());

            String selectedLocation = listSimulationLocation.get(randomIndex);
            return selectedLocation;
        } else {
            return null;
        }
    }
	
	public String selectRandomCategory () {        
        if (!listSimulationCategory.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(listSimulationCategory.size());

            String selectedLocation = listSimulationCategory.get(randomIndex);
            return selectedLocation;
        } else {
            return null;
        }
    }
	
	public String generateRandomText(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder text = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            text.append(characters.charAt(index));
        }

        return text.toString();
    }

}
