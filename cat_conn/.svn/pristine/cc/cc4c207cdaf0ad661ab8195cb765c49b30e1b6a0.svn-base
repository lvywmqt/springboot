/**
 * 
* 类名称： CatRequestResponseTimeUtil.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年6月6日
* @version 2.0
 */
package com.cgltech.cat_conn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author User
 *
 */
public class CatRequestResponseTimeUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CatRequestResponseTimeUtil.class);

	private static final String REQUEST_TIME = "\\[log4j\\].([\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})";
	private static final String DEVICE_NO = "\"deviceNo\":\"([^\"]*)\"";
	private static final String METHOD = "\"method\":\"([^\"]*)\"";
	private static final String TIME = "\"time\":\"([^\"]*)\"";

	private static final DecimalFormat RUNTIME_FORMAT = new DecimalFormat(",###");
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

	private static Map<String, String> getStatus = new HashMap<>();
	private static Map<String, String> returnGetStatus = new HashMap<>();
	
	public static void load(String logPath) throws IOException{
		
		File logDir = new File(logPath);

		for (File file : logDir.listFiles()) {
			if (file.isFile()) {
				FileReader fileReader = new FileReader(file);

				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
					
					parse(line);
				}

				bufferedReader.close();
			}
		}
	}

	private static void parse(String line){
		
		Pattern pattern = Pattern.compile(REQUEST_TIME);
		Matcher matcher = pattern.matcher(line);
		String requestTime = null;
		if (matcher.find()) {
			requestTime = matcher.group(1);
		}
		pattern = Pattern.compile(DEVICE_NO);
		matcher = pattern.matcher(line);
		String deviceNo = null;
		if (matcher.find()) {
			deviceNo = matcher.group(1);
		}
		
		pattern = Pattern.compile(METHOD);
		matcher = pattern.matcher(line);
		String method = null;
		if (matcher.find()) {
			method = matcher.group(1);
		}
		
		pattern = Pattern.compile(TIME);
		matcher = pattern.matcher(line);
		String time = null;
		if (matcher.find()) {
			time = matcher.group(1);
		}
		if (method != null && time != null) {
			switch (method) {
				case "getStatus":{
					getStatus.put(deviceNo + time, requestTime);
					break;
				}
				case "return_getStatus":{
					returnGetStatus.put(deviceNo + time, requestTime);
					break;
				}
			}
		}
	}
	
	public static void calculateGetStatus(
			Map<String, String> requestTimes, 
			Map<String, String> responseTimes){
		
		int totalTime = 0;
		int averageTime = 0;
		int times = 0;
		
		long minTime = Integer.MAX_VALUE;
		long maxTime = Integer.MIN_VALUE;
		
		Iterator<Entry<String, String>> iterator = requestTimes.entrySet().iterator();
		
		while(iterator.hasNext()){
			Entry<String, String> entryGetStatus = iterator.next();
			String requestTime = entryGetStatus.getValue();
			String responseTime = responseTimes.get(entryGetStatus.getKey());
			
			if (requestTime != null && responseTime != null) {
				
				long eachtime = 0;
		
				Date requestDate = null;
				Date responseDate = null;
				
		        try {
		            requestDate = DATE_FORMAT.parse(requestTime);
		            responseDate = DATE_FORMAT.parse(responseTime);
		        } catch(ParseException px) {
		        	px.printStackTrace();
		            break;
		        }
		        
		        eachtime = responseDate.getTime() - requestDate.getTime();
	            
	            totalTime += eachtime;
				
				minTime = minTime > eachtime?eachtime:minTime;
				maxTime = maxTime < eachtime?eachtime:maxTime;

				times++;
			}
		}
		
		averageTime = totalTime/times;
		LOGGER.info("总共执行时间：{}毫秒", RUNTIME_FORMAT.format(totalTime));
		LOGGER.info("总共执行次数：{}次", RUNTIME_FORMAT.format(times));
		LOGGER.info("平均执行时间：{}毫秒", RUNTIME_FORMAT.format(averageTime));
		LOGGER.info("最慢执行时间：{}毫秒", RUNTIME_FORMAT.format(maxTime));
		LOGGER.info("最快执行时间：{}毫秒", minTime);
	}
	public static void main(String[] args) throws IOException{
		
		load("C:\\Users\\User\\Downloads\\logs");
		
		calculateGetStatus(getStatus, returnGetStatus);
	}
}
