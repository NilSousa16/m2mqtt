package br.ufba.dcc.wiser.m2mqtt.impl;

import br.ufba.dcc.wiser.m2mqtt.simulation.DeviceSimulator;

public class DeviceStatusUpdater implements Runnable {

	private volatile boolean running = true;
	private DeviceSimulator deviceSimulator;
	private Thread updaterThread;

	public DeviceStatusUpdater(DeviceSimulator deviceSimulator) {
		this.deviceSimulator = deviceSimulator;
	}

	public void start() {
		updaterThread = new Thread(this);
		updaterThread.start();
	}

	public void stop() {
		running = false;
		if (updaterThread != null) {
			try {
				updaterThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			
			deviceSimulator.deviceMonitor();
			
			// Wait 5 seconds between verifications
			try {
				Thread.sleep(5000); 
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

}
