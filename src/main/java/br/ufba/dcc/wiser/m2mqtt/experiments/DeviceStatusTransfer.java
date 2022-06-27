package br.ufba.dcc.wiser.m2mqtt.experiments;

import java.util.Calendar;

public class DeviceStatusTransfer {

	private String idDevice;

	private Calendar date;

	private String situation;

	public String getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

}
