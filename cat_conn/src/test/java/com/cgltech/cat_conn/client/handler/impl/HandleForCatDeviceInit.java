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
public class HandleForCatDeviceInit implements IHandleCatClient {

	/* (non-Javadoc)
	 * @see com.cgltech.cat_conn.client.handler.IHandleCatClient#handle(com.cgltech.cat_conn.entity.TransmissionParameterVO)
	 */
	@Override
	public String handle(TransmissionParameterVO transmissionParameterVO) {
		// TODO Auto-generated method stub
		//{"data":{"fwVersion":"xx1","hwVersion":"xxx","serialNo":"yyy","devtype":"TCM01M1" },"deviceNo":"201803250001","method":"deviceInit", "time":"123456789"}
		return null;
	}

}
