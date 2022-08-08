package br.ufba.dcc.wiser.m2mqtt.communication;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.google.gson.Gson;

import br.ufba.dcc.wiser.m2model.model.Device;
import br.ufba.dcc.wiser.m2model.model.DeviceStatusTransfer;
import br.ufba.dcc.wiser.m2mqtt.utils.Consts;

public class ServerCommunication {

	public boolean send(Object object) throws Exception{
		try {
			Gson gson = new Gson();
			
			String uri = "";

			String jsonObject = gson.toJson(object);
			
			if(object instanceof Device) {
				uri = "m2fot/fot-device-register/";
			} else if(object instanceof DeviceStatusTransfer) {
				uri = "m2fot-status/fot-device-status/";
			}
			
			System.out.println("ServerCommunication - Dados sendo enviados: " + jsonObject);
			System.out.println("ServerCommunication - Dados enviados para : " + uri);

			if (this.register("http://" + Consts.SERVER_IP + ":8181/cxf/" + uri, jsonObject)) {
				return true;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return false;
	}

	private boolean register(String url, String jsonObject) throws Exception {
		try {
			HttpClient client = new HttpClient();
			PostMethod mPost = new PostMethod(url);
			mPost.setRequestEntity(new StringRequestEntity(jsonObject, null, null));

			Header mtHeader = new Header();
			mtHeader.setName("content-type");
			mtHeader.setValue("application/x-www-form-urlencoded");
			mtHeader.setName("accept");
			mtHeader.setValue("application/json");
			
			mPost.addRequestHeader(mtHeader);
			client.executeMethod(mPost);
			
			mPost.releaseConnection();
			
			if (mPost.getStatusCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("M2MQTT - Exception in adding bucket...");
			throw new Exception(e);
		}
		return false;
	}

}
