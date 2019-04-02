package com.cgltech.cat_ip_tcp.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ToolUtil {

	public ToolUtil() {
	}
	
	//读取某个txt文件
	//fileName示例："G:/readtxt/txt.txt"
	public static String readTxtFile(String fileName) {
		// 读取文件
		BufferedReader br = null;
		StringBuffer sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8")); // 这里可以控制编码
			sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String data = new String(sb); // StringBuffer ==> String
		System.out.println("数据为==> " + data);
		return data;
	}
	//根据偏移量和长度，拷贝bytes数组中的数据。
	public static byte[] getSectionBytes(byte[] bytes,int offset,int length){
		int total = bytes.length;
		int minusValue = total-offset;
		//如果从偏移量开始到末尾的长度（minusValue）大于每次需要获取的长度（length），那么按获取的长度截取。
		if(minusValue>length){
			byte[] readData = new byte[length];
			System.arraycopy(bytes,  
					offset,  
	                readData,  
	                0,  
	                length);
			return readData;
		}else{//如果从偏移量开始到末尾的长度（minusValue）小于或者等于每次需要获取的长度（length），那么按minusValue截取。
			byte[] readData = new byte[minusValue];
			System.arraycopy(bytes,  
					offset,  
	                readData,  
	                0,  
	                minusValue);
			return readData;
		}
	}
}
