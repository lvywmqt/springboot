package com.cgltech.cat_conn.client.handler;

import java.util.HashMap;
import java.util.Map;

import com.cgltech.cat_conn.client.handler.impl.HandleForCatDeviceInit;
import com.cgltech.cat_conn.client.handler.impl.HandleForCatUploadCutTicketResult;
import com.cgltech.cat_conn.client.handler.impl.HandleForCatUploadDeviceStatus;
import com.cgltech.cat_conn.client.handler.impl.HandleForCutTicket;
import com.cgltech.cat_conn.client.handler.impl.HandleForGetGprsStatus;
import com.cgltech.cat_conn.client.handler.impl.HandleForGetStatus;
import com.cgltech.cat_conn.client.handler.impl.HandleForGetTicketLength;
import com.cgltech.cat_conn.client.handler.impl.HandleForGetVersion;
import com.cgltech.cat_conn.client.handler.impl.HandleForNeedUpdateFirmware;
import com.cgltech.cat_conn.client.handler.impl.HandleForReadyReceiveInstruction;
import com.cgltech.cat_conn.client.handler.impl.HandleForReboot;
import com.cgltech.cat_conn.client.handler.impl.HandleForRogerReturnCutTicket;
import com.cgltech.cat_conn.client.handler.impl.HandleForUpdateTicketLength;

public class CatClientHandlerFactory {
	
	private static Map<String, IHandleCatClient> HANDLES = new HashMap<>();
	static {
		
		HANDLES.put("return_deviceInit", new HandleForCatDeviceInit());
		HANDLES.put("return_uploadDeviceStatus", new HandleForCatUploadDeviceStatus());
		HANDLES.put("return_uploadCutTicketResult", new HandleForCatUploadCutTicketResult());

		HANDLES.put("cutTicket", new HandleForCutTicket());
		HANDLES.put("roger_return_cutTicket", new HandleForRogerReturnCutTicket());

		HANDLES.put("getTicketLength", new HandleForGetTicketLength());
		HANDLES.put("updateTicketLength", new HandleForUpdateTicketLength());
		HANDLES.put("getStatus", new HandleForGetStatus());
		HANDLES.put("getVersion", new HandleForGetVersion());
		HANDLES.put("getGprsStatus", new HandleForGetGprsStatus());
		HANDLES.put("reboot", new HandleForReboot());
		HANDLES.put("need_updateFirmware", new HandleForNeedUpdateFirmware());
		HANDLES.put("ready_receive_instruction", new HandleForReadyReceiveInstruction());

	}
	public static IHandleCatClient getCatHandler(String key) {
		return HANDLES.get(key);
	}
	
}