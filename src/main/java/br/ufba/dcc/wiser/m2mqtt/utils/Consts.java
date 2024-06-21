package br.ufba.dcc.wiser.m2mqtt.utils;

/**
 * Constants used in the M2 mqtt
 * 
 * @author Nilson Rodrigues Sousa
 */
public class Consts {

	/**
	 * mqtt broker address
	 */
	public static final String BROKER_IP = "tcp://192.168.0.6:1883";

	/**
	 * Server IP address
	 */
	public static final String SERVER_IP = "192.168.0.6";

	/**
	 * Topic used to send device registration data
	 */
	public static final String SEND_DEVICE_REGISTER = "manager/device/register";

	/**
	 * Topic used to send data stream of modifications that occur on devices
	 */
	public static final String SEND_DEVICE_INFO = "manager/device/send-data";

	/*
	 * Topic used to send individual data from a device when requested
	 */
	public static final String SEND_DEVICE_SETTINGS = "manager/device/settings/send";

	/**
	 * Topic used to receive requests with settings that should be changed across
	 * devices
	 */
	public static final String RECEIVE_DEVICE_MODIFY_SETTINGS = "manager/device/settings/modify";

	/*
	 * Topic used to receive requests for information from a device specific
	 */
	public static final String RECEIVE_DEVICE_REQUEST = "manager/device/settings/request";

}
