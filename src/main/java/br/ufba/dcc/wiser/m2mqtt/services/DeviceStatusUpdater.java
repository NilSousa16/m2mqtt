package br.ufba.dcc.wiser.m2mqtt.services;

import br.ufba.dcc.wiser.m2mqtt.simulation.DeviceSimulator;

/**
 * The DeviceStatusUpdater class is responsible for periodically updating the
 * status of devices using the provided DeviceSimulator.
 * 
 * @author Nilson Rodrigues Sousa
 */
public class DeviceStatusUpdater implements Runnable {

	private volatile boolean running = true;
	private DeviceSimulator deviceSimulator;
	private Thread updaterThread;

	/**
	 * Constructs a DeviceStatusUpdater with the specified DeviceSimulator.
	 *
	 * @param deviceSimulator The DeviceSimulator used to monitor and update device
	 *                        statuses.
	 */
	public DeviceStatusUpdater(DeviceSimulator deviceSimulator) {
		this.deviceSimulator = deviceSimulator;
	}

	/**
	 * Starts the device status updater thread.
	 */
	public void start() {
		updaterThread = new Thread(this);
		updaterThread.start();
	}

	/**
	 * Stops the device status updater thread. Waits for the thread to finish
	 * execution before returning.
	 */
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

	/**
	 * The main logic for the updater thread. Periodically calls the deviceMonitor
	 * method of the DeviceSimulator to update device statuses.
	 */
	@Override
	public void run() {
		while (running) {

			deviceSimulator.deviceMonitor();

			// Wait 15 seconds between verifications
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

}
