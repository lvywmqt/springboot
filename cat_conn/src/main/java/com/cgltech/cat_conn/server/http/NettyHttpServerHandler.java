package com.cgltech.cat_conn.server.http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.listener.ApplicationContextFactory;
import com.cgltech.cat_conn.server.cat.CatServerManager;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.server.cat.future.SyncWrite;
import com.cgltech.cat_conn.service.TicketCutService;
import com.cgltech.cat_conn.service.dto.AddTicketCutDto;
import com.cgltech.cat_conn.service.impl.TicketCutServiceImpl;
import com.cgltech.cat_conn.util.CatPacketData;
import com.cgltech.cat_conn.util.CatSignatureUtil;
import com.cgltech.cat_conn.util.SingletonProperty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;


public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpServerHandler.class);
	
	private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");

	private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");

	private static final AsciiString CONNECTION = new AsciiString("Connection");

	private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");

	
	@SuppressWarnings("unused")
	private static enum IsTest {
		YES("1", "测试"), NO("0", "非测试");
		
		private String value;
		private String desc;
		
		private IsTest(String value, String desc){
			this.value = value;
			this.desc = desc;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		FullHttpRequest req = null;
		CatPacketData catPacketData = null;
		
		if (msg instanceof FullHttpRequest) {

			req = (FullHttpRequest) msg;// 客户端的请求对象

			// 根据不同的请求做不同的处理(路由分发)，只处理POST方法
			if (req.method() == HttpMethod.POST) {
				
				String content = parseJosnRequest(req);
				LOGGER.info("接收cat_biz端的字符串:[{}]", content);
				
				catPacketData = handlePost(ctx, req, content);
			} else {
				catPacketData = new CatPacketData();
				catPacketData.setCode(404);
				catPacketData.setMessage("not found!");
			}
		} else {
			catPacketData = CatPacketData.newFailed("系统内部错误");
		}

		if (catPacketData != null) {
			ResponseJson(ctx, req, catPacketData);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		
		LOGGER.error("服务端处理异常", cause);

		ctx.close();
	}

	private CatPacketData handlePost(ChannelHandlerContext ctx, FullHttpRequest req, String content){

		if(!isTest()){//不是测试环境,验证签名
			if(content.length() > CatSignatureUtil.SIGN_LENGTH){//验证MD5签名长度
				String sign = CatSignatureUtil.getSign(content);
				content = CatSignatureUtil.getContent(content);
				if (!CatSignatureUtil.checkSign(content, sign)) {
					CatPacketData catPacketData = new CatPacketData();
					catPacketData.setMessage("验签失败");
					return catPacketData;
				}
			}else{
				CatPacketData catPacketData = new CatPacketData();
				catPacketData.setMessage("数据不合法");
				return catPacketData;
			}
		}
		
		CatPacketData requestPacketData = JSONObject.parseObject(
				content, CatPacketData.class);

		JSONObject requestJson = (JSONObject)requestPacketData.getData();
		
		return handlePost(ctx, req, requestJson);
	}
	
	private CatPacketData handlePost(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject requestJson) {

		CatPacketData catPacketData = null;
		JSONObject responseJson = new JSONObject();
		
		String methodName = requestJson.get("method").toString();
		String deviceNo = requestJson.get("deviceNo").toString();

		responseJson.put("deviceNo", deviceNo);
		responseJson.put("method", methodName);

		if (req.uri().equals("/sale")) {
			catPacketData = handleSale(ctx, req, requestJson, responseJson, methodName, deviceNo);
		} else if (req.uri().equals("/update")) {
			catPacketData = handleUpdate(ctx, req, requestJson, responseJson, methodName, deviceNo);
		} else if (req.uri().equals("/conf")) {
			catPacketData = handleConf(ctx, req, requestJson, responseJson, methodName, deviceNo);
		} else if (req.uri().equals("/query")) {
			catPacketData = handleQuery(ctx, req, requestJson, responseJson, methodName, deviceNo);
		}

		return catPacketData;
	}

	private CatPacketData handleSale(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject requestJson,
			JSONObject responseJson, String methodName, String deviceNo) {
		
		CatPacketData catPacketData = null;
		
		TransmissionParameterVO tp = JSONObject.parseObject(requestJson.toString(), TransmissionParameterVO.class);
		
		// cat_biz检查是否在线
		if (methodName.equals("checkFlagOnline")) {
			Map<String, Object> map = new HashMap<>();
			map.put("flagOnline", "0");
			// 设备在线
			if (CatServerManager.INSTANCE.isOnline(deviceNo)) {
				map.put("flagOnline", "1");
				responseJson.put("data", map);
			} else {
				responseJson.put("data", map);
			}
			catPacketData = new CatPacketData();
			catPacketData.setData(responseJson);
		} else if (methodName.equals("cutTicket_cat_biz")) {// cat_biz请求出票

			// 转换ticketLength由数值型改为字符串类型
			((JSONObject) tp.getData()).put("requestSaleNum",
					String.valueOf(((JSONObject) tp.getData()).get("requestSaleNum")));
			
			this.dealCutTicketHttpRequest(ctx, req, tp, responseJson);

		} else if (methodName.equals("cutTicket")) {// cat_biz test请求出票

			// 转换ticketLength由数值型改为字符串类型
			((JSONObject) tp.getData()).put("requestSaleNum",
					String.valueOf(((JSONObject) tp.getData()).get("requestSaleNum")));

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
		}

		return catPacketData;
	}

	private CatPacketData handleConf(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject requestJson,
			JSONObject responseJson, String methodName, String deviceNo) {

		CatPacketData catPacketData = null;
		TransmissionParameterVO tp = JSONObject.parseObject(requestJson.toString(), TransmissionParameterVO.class);
		
		// cat_biz请求换票
		if (methodName.equals("updateTicketLength")) {

			// 转换ticketLength由数值型改为字符串类型
			((JSONObject) tp.getData()).put("ticketLength",
					String.valueOf(((JSONObject) tp.getData()).get("ticketLength")));
			
			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
		} else if (methodName.equals("reboot")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
		}else if (methodName.equals("deleteresult")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));

		}
		
		return catPacketData;
	}

	private CatPacketData handleUpdate(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject requestJson,
			JSONObject responseJson, String methodName, String deviceNo) {

		CatPacketData catPacketData = null;
		
		TransmissionParameterVO tp = JSONObject.parseObject(requestJson.toString(), TransmissionParameterVO.class);

		if (methodName.equals("need_updateFirmware")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
		}
		
		return catPacketData;
	}

	private CatPacketData handleQuery(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject requestJson,
			JSONObject responseJson, String methodName, String deviceNo) {
		
		CatPacketData catPacketData = null;
		
		TransmissionParameterVO tp = JSONObject.parseObject(requestJson.toString(), TransmissionParameterVO.class);

		// cat_biz请求换票
		if (methodName.equals("getStatus")) {
			
			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
			
		} else if (methodName.equals("getGprsStatus")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));

		} else if (methodName.equals("getVersion")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));

		} else if (methodName.equals("getOnlineList")) {

			responseJson.put("data", CatServerManager.INSTANCE.getAllDevices());
			
			catPacketData = new CatPacketData();
			catPacketData.setData(responseJson);

		} else if (methodName.equals("getOnlineTotal")) {

			responseJson.put("data", CatServerManager.INSTANCE.getSessionTotal());
			
			catPacketData = new CatPacketData();
			catPacketData.setData(responseJson);

		} else if (methodName.equals("getTicketLength")) {

			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));

		} else if (methodName.equals("checkDeviceOnline")) {

			Map<String, String> map = new HashMap<>();
			if (CatServerManager.INSTANCE.isOnline(deviceNo)) {
				map.put("flag", "online");
			} else {
				map.put("flag", "offline");
			}
			responseJson.put("data", map);
			
			catPacketData = new CatPacketData();
			catPacketData.setData(responseJson);
		}else if(methodName.equals("queryResult")){
			ResponseJson(ctx, req, this.dealHttpRequest(tp, responseJson));
		}

		return catPacketData;
	}

	private String parseJosnRequest(FullHttpRequest request) {

		ByteBuf jsonBuf = request.content();

		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);

		if (jsonStr != null && !jsonStr.equals("")) {
			return jsonStr;
		} else {
			return null;
		}
	}
	
	private boolean isTest(){
		String isTest = SingletonProperty.getInstance().getPropertyValue("systemConfig.isTest");
		return IsTest.YES.getValue().equals(isTest);
	}
	
	/*
	 * 响应HTTP的请求
	 * 
	 * @param ctx
	 * 
	 * @param req
	 * 
	 * @param jsonStr
	 */

	private void ResponseJson(ChannelHandlerContext ctx, FullHttpRequest req, CatPacketData catPacketData) {
		
		String jsonStr = JSONObject.toJSONString(catPacketData, SerializerFeature.WriteMapNullValue);
		
		//如果是正式服务器，那么需要添加签名字段sign到前面
		if(!isTest()){
			String sign = CatSignatureUtil.sign(jsonStr);
			jsonStr = sign + jsonStr;
		}
		
		LOGGER.info("返回cat_biz端的字符串：[{}]", jsonStr);
		
		boolean keepAlive = HttpUtil.isKeepAlive(req);
		byte[] jsonBytes = jsonStr.getBytes();
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(jsonBytes));
		response.headers().set(CONTENT_TYPE, "text/json");
		response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set("Access-Control-Allow-Origin", "*");
		response.headers().set("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");

		if (!keepAlive) {
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		} else {
			response.headers().set(CONNECTION, KEEP_ALIVE);
			ctx.writeAndFlush(response);
		}
	}

	public CatPacketData dealHttpRequest(TransmissionParameterVO tp, JSONObject responseJson) {
		
		long timeout = Long.parseLong(
				SingletonProperty.getInstance().getPropertyValue("systemConfig.SyncWriteFuture.timeout"));

		String time = String.valueOf(System.currentTimeMillis());
		tp.setTime(time);
		
		CatPacketData catPacketData = SyncWrite.writeAndSync(tp, timeout, responseJson);

		return catPacketData;
	}

	public void dealCutTicketHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req, TransmissionParameterVO tp,
			final JSONObject responseJson) {
		
		try {
			TicketCutService ticketCutService = ApplicationContextFactory.INSTANCE.getService(TicketCutServiceImpl.class);
			
			AddTicketCutDto addTicketCutDto = new AddTicketCutDto();
			
			addTicketCutDto.setDeviceNo(tp.getDeviceNo());
			addTicketCutDto.setMethod(tp.getMethod());
			
			JSONObject requestData = tp.getData();
			addTicketCutDto.setOrderNo(requestData.getString("orderNo"));
			addTicketCutDto.setTicketLength(requestData.getInteger("ticketLength"));
			
			Object callBackUrl = requestData.get("callBackUrl");
			
			if(callBackUrl == null){
				callBackUrl = SingletonProperty.getInstance().getPropertyValue("systemConfig.tcp.server.cat_biz.url.respondCutTicket");
			}
			addTicketCutDto.setNotifyUrl(callBackUrl.toString());

			addTicketCutDto.setRequestSaleNum(requestData.getInteger("requestSaleNum"));
			addTicketCutDto.setTimestamp(tp.getTime());

			ticketCutService.addTicketCut(addTicketCutDto);
			
			CatPacketData catPacketData = CatPacketData.newSuccess(responseJson);

			ResponseJson(ctx, req, catPacketData);
		} catch (Exception e) {
			LOGGER.error("[deviceNo:{}]:新增出票订单错误[{}]", tp.getDeviceNo(), tp, e);
			CatPacketData catPacketData = CatPacketData.newFailed("出票错误", responseJson);
			ResponseJson(ctx, req, catPacketData);
		}

	}

}
