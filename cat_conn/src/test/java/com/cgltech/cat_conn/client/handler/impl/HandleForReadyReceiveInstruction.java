/**
 * 
* 类名称： CatDeviceInit.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月16日
* @version 2.0
 */
package com.cgltech.cat_conn.client.handler.impl;

import com.cgltech.cat_conn.client.handler.IHandleCatClient;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

/**
 * @author User
 *
 */
public class HandleForReadyReceiveInstruction implements IHandleCatClient {

	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {

		//{"data":{"received":"true"},
		//"deviceNo":"201803250001","method":"ready_receive_instruction", "time":""} 

		return null;
	}

}
