package com.cgltech.cat_conn.server.cat.handle;

import java.util.HashMap;
import java.util.Map;

import com.cgltech.cat_conn.server.cat.handle.impl.*;

public class CatServerHandlerFactory {
	
	private static Map<String, IHandleCatServer> HANDLES = new HashMap<>();
	static {
		HANDLES.put("uploadDeviceStatus", new HandleForUploadDeviceStatus());
		HANDLES.put("return_cutTicket", new HandleForCutTicket());
		HANDLES.put("return_getTicketLength", new HandleForGetTicketLength());
		HANDLES.put("return_updateTicketLength", new HandleForUpdateTicketLength());
		HANDLES.put("return_getStatus", new HandleForGetStatus());
		HANDLES.put("return_getVersion", new HandleForGetVersion());
		HANDLES.put("return_getGprsStatus", new HandleForGetGprsStatus());
		HANDLES.put("return_reboot", new HandleForReboot());
		HANDLES.put("return_need_updateFirmware", new HandleForNeedUpdateFirmware());
		HANDLES.put("ready_receive_instruction", new HandleForReadyReceiveInstruction());
		HANDLES.put("return_queryResult", new HandleForQueryResult());
		HANDLES.put("return_deleteresult", new HandleForDeleteTerminalResult());
	}
	public static IHandleCatServer getCatHandler(String key) {
		return HANDLES.get(key);
	}
	
}