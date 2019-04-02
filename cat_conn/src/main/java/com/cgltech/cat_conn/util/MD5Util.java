package com.cgltech.cat_conn.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

public class MD5Util {
	/**
	 * 1.对文本进行32位小写MD5加密
	 * 
	 * @param plainText
	 *            要进行加密的文本
	 * @return 加密后的内容
	 */
	public static String textToMD5L32(String plainText) {
		String result = null;
		// 首先判断是否为空
		if (!StringUtils.isNotEmpty(plainText)) {
			return null;
		}
		try {
			// 首先进行实例化和初始化
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 得到一个操作系统默认的字节编码格式的字节数组
			byte[] btInput;
			try {
				btInput = plainText.getBytes("UTF-8");
				// 对得到的字节数组进行处理
				md.update(btInput);
				// 进行哈希计算并返回结果
				byte[] btResult = md.digest();
				// 进行哈希计算后得到的数据的长度
				StringBuffer sb = new StringBuffer();
				for (byte b : btResult) {
					int bt = b & 0xff;
					if (bt < 16) {
						sb.append(0);
					}
					sb.append(Integer.toHexString(bt));
				}
				result = sb.toString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 2.对文本进行32位MD5大写加密
	 * 
	 * @param plainText
	 *            要进行加密的文本
	 * @return 加密后的内容
	 */
	public static String textToMD5U32(String plainText) {
		if (!StringUtils.isNotEmpty(plainText)) {
			return null;
		}
		String result = textToMD5L32(plainText);
		return result.toUpperCase();
	}

	//登录密码加密
	public static final String Encryption(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}
	public static void main(String[] args) {
		String newpass = "123456";
		newpass = Encryption(newpass.trim());
		System.out.println(newpass);
	}
}