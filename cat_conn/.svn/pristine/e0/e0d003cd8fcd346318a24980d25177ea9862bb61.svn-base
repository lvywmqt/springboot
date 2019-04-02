/**
 * 
* 类名称： CatSignatureUtil.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月24日
* @version 2.0
 */
package com.cgltech.cat_conn.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author User
 *
 */
public class CatSignatureUtil {
	
	public static final int SIGN_LENGTH = 32;
	
	public static String getContent(String content){
		return content.substring(SIGN_LENGTH);
	}
	
	public static String getSign(String content){
		return content.substring(0, SIGN_LENGTH);
	}

	public static  String sign(String content) {
		
		String signKey = SingletonProperty.getInstance().getPropertyValue("systemConfig.http.request.sign.key");
		
		return MD5Util.textToMD5L32(content + signKey);
	}

	public static  boolean checkSign(String content, String sign) {

		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(sign)) {
			return false;
		}
		String signature = sign(content);
		return sign.equals(signature);
	}
}
