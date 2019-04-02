package com.cgltech.cat_ip_tcp.util;

/* 
后台返回前台结果类。
实例举例：
[{	
	"code":200,
	"data":{"column":"666"},
	"message":"success"
}]
初始化的默认值：
		ActionResult ar = new ActionResult();
		JSONObject jsonObject = JSONObject.fromObject(ar); 
		System.out.println(jsonObject);//{"code":0,"data":null,"message":""}
*/
public class ActionResult {
	private int code;//200：成功，400：请求错误，
	private String message;
	private Object data;
	
	public ActionResult() {
		
	}
	
	public ActionResult(int code) {
		this.code = code;
	}
	
	public ActionResult(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public ActionResult(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}
