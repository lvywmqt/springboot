package com.cgltech.cat_ip_tcp.tcp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//import org.apache.log4j.Logger;
//import org.apache.log4j.spi.LoggerFactory;

import com.cgltech.cat_ip_tcp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_ip_tcp.entity.TransmissionParameterVO;
import com.cgltech.cat_ip_tcp.handle.DruidJdbcUtils;
import com.cgltech.cat_ip_tcp.handle.SingletonProperty;
//import com.cgltech.cat_ip_tcp.util.logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;


public class MyChannelHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory
            .getLogger(MyChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        /**
         *  context 格式
         * {"method":"deviceInit","deviceNo":"2018050580221","data":{"fwVersion":"50201330","hwVersion":"50200301","serialNo":"2018050580221","type":"TCM01M1","flag":"true","errcode":"0"
         ,"ccid":"123456789","signalQuality":"30","brokenNetworkReconnection":"0"}
         */
        String data = msg.toString();
        if (data.equals("1")) {
            logger.info("收到心跳!");
            return;
        }
        logger.info("*****************channelRead0收到数据:[{}]", data);
        TransmissionParameterVO tp = null;
        try {
            tp = JSONObject.parseObject(data, TransmissionParameterVO.class);
            String methodName = tp.getMethod();
            if (methodName.equals("deviceInit")) {
                this.deviceInit(context, tp);
            } else if (methodName.equals("updateFirmware")) {
                this.updateFirmware(context, tp);
            }
        } catch (Exception e) {
            logger.error("channelRead0，转换json出错！原始数据是：【{}】", data, e);
//    			context.channel().writeAndFlush(new String("坏请求".getBytes("UTF-8")));
            NettyHandlerUtil.appendTagAndWriteAndFlush(context.channel(), "error\n");
            return;
        }

    }


    public void deviceInit(ChannelHandlerContext context, TransmissionParameterVO tpVO) {
        ActionResult ar = new ActionResult();
        //设备编号不符合规则
        if (false) {//TODO 判断设备编号的规则
            ar.setCode(400);
            ar.setMessage("access denied!");
            ar.setData(null);
            //riteMapNullValue设置输出值为null的字段
            String jsonString = JSONObject.toJSONString(ar, SerializerFeature.WriteMapNullValue);
            return;
        }

//sql语句格式化如下：

//    	SELECT
//    	device_no,
//    	version_current,
//    	version_require,
//    	server_address_update,
//    	server_address_connection,
//    	flag_test,
//    	ticket_length,
//    	flag_update
//    FROM
//    	machine_cat
//    WHERE
//    	device_no = ?

        String device_no = tpVO.getDeviceNo();
        final String sql = "SELECT device_no, version_current, version_require, server_address_update, server_address_connection, flag_test, ticket_length, flag_update FROM machine_cat WHERE device_no = ?";
        List<Object> list = DruidJdbcUtils.executeQuery(sql, new Object[]{device_no});

        String sqlConfig = "SELECT config_key,config_value FROM machine_cat_config";
        List<Object> configlist = DruidJdbcUtils.executeQuery(sqlConfig, new Object[]{});

        Map<String, String> configMap = new HashMap<String, String>();

        //记录连接日志
        //新版本固件ccid、signalQuality不为空
        if (tpVO.getData().getString("ccid") != null || tpVO.getData().getString("signalQuality") != null) {

            recordConnRecord(tpVO);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (configlist != null && configlist.size() != 0) {
            for (int i = 0; i < configlist.size(); i++) {
                resultMap = (Map<String, Object>) configlist.get(i);

                String key = (String) resultMap.get("config_key");
                String value = (String) resultMap.get("config_value");
                configMap.put(key, value);
            }
        }
        //没有这个device_no的记录，设备没有录入后台系统。
        if (list != null && list.size() == 0) {
            ar.setCode(400);
            ar.setMessage("access denied!!");
            ar.setData(null);
            //riteMapNullValue设置输出值为null的字段
            String jsonString = JSONObject.toJSONString(ar, SerializerFeature.WriteMapNullValue);
            logger.info("device not exist in database:" + device_no);
            StringBuffer sb = new StringBuffer();
            sb.append("{\"data\":{\"fwVersion\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.fwVersion"));
            sb.append(configMap.get("fwVersion"));
            sb.append("\",\"ticketLength\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.ticketLength"));
            sb.append(configMap.get("ticketLength"));
            sb.append("\",\"needUpdate\":\"false\"");
            sb.append(",\"updateServerIP\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.updateServerIP"));\
            sb.append(configMap.get("updateServerIP"));
            sb.append("\",\"updateServerPort\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.updateServerPort"));
            sb.append(configMap.get("updateServerPort"));
            sb.append("\",\"connectionServerIP\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.connectionServerIP"));
            sb.append(configMap.get("connectionServerIP"));
            sb.append("\",\"connectionServerPort\":\"");
            //sb.append(SingletonProperty.getInstance().getPropertyValue("systemConfig.catReturnString.connectionServerPort"));
            sb.append(configMap.get("connectionServerPort"));
            sb.append("\"},\"deviceNo\":\"");
            sb.append(device_no);
            sb.append("\",\"method\":\"return_deviceInit\",\"time\":\"\"}");
            String returnData = sb.toString();
            logger.info("no device return data:[{}]", returnData);
            NettyHandlerUtil.appendTagAndWriteAndFlush((SocketChannel) context.channel(), returnData);

            return;
        } else if (list != null && list.size() > 0) {

            JSONObject data = tpVO.getData();
            String fwVersion = data.getString("fwVersion");


            Map<String, Object> map = (Map<String, Object>) list.get(0);
//            System.out.println("查询结果：" + map.get("ticket_length"));
            logger.info("设备编号：" + tpVO.getDeviceNo() + "的查询结果：" + map);
            String version_current = (String) map.get("version_current");
            String version_require = (String) map.get("version_require");
            //ticketLength的数据类型为String，因为猫客户端硬件的编程语言，软件设计是这样的。就把票长设置为字符串类型
            String ticketLength = String.valueOf(map.get("ticket_length"));
            Integer flag_update = (Integer) map.get("flag_update");

            //猫的版本号和数据库中存储的版本号不同，更新version_current,以及激活时间
            if (version_current == null || !version_current.equals(fwVersion)) {
                String updatesql = "update machine_cat set version_current=?,active_time=? where device_no = ?";
                int key = DruidJdbcUtils.executeUpdate(updatesql, new Object[]{fwVersion, new Date(), tpVO.getDeviceNo()});
                logger.info("方法" + "deviceInit" + "设备编号为" + tpVO.getDeviceNo() + "更新数据库的version_current为:" + fwVersion + ","
                        + "更新数据库的active_time为:" + new Date());
            } else {
                String updatesql = "update machine_cat set active_time=? where device_no = ?";
                int key = DruidJdbcUtils.executeUpdate(updatesql, new Object[]{new Date(), tpVO.getDeviceNo()});
                logger.info("方法" + "deviceInit" + "设备编号为" + tpVO.getDeviceNo() + "更新数据库的active_time为:" + new Date());
            }

            JSONObject deviceInitDataObject = new JSONObject(true);//设置为true，以put的顺序排列
            deviceInitDataObject.put("fwVersion", version_require);
            deviceInitDataObject.put("ticketLength", ticketLength);
            if (!version_require.equals(fwVersion)) {
                deviceInitDataObject.put("needUpdate", "true");
            } else {
                deviceInitDataObject.put("needUpdate", "false");
            }
            //这里增加这个判断是为了：当字段flag_update为0时候，如果客户端版本与服务端version_require不一致，设置"needUpdate"为"false"
            if (flag_update == 0) {
                deviceInitDataObject.put("needUpdate", "false");
            }

            //开始获取更新服务器地址
            String UpdateServerAddress = (String) map.get("server_address_update");
            String[] array_UpdateServerAddress = UpdateServerAddress.split(":");
            String UpdateServerAddressIP = array_UpdateServerAddress[0];
            String UpdateServerAddressPort = array_UpdateServerAddress[1];

            deviceInitDataObject.put("updateServerIP", UpdateServerAddressIP);
            deviceInitDataObject.put("updateServerPort", UpdateServerAddressPort);
            //开始获取长连接服务器地址
            String ConnectionServerAddress = (String) map.get("server_address_connection");
            String[] array_ConnectionServerAddress = ConnectionServerAddress.split(":");
            String ConnectionServerAddressIP = array_ConnectionServerAddress[0];
            String ConnectionServerAddressPort = array_ConnectionServerAddress[1];

            deviceInitDataObject.put("connectionServerIP", ConnectionServerAddressIP);
            deviceInitDataObject.put("connectionServerPort", ConnectionServerAddressPort);

            TransmissionParameterVO return_tpVO = new TransmissionParameterVO();
            return_tpVO.setData(deviceInitDataObject);
            return_tpVO.setDeviceNo(device_no);
            return_tpVO.setMethod("return_deviceInit");
            return_tpVO.setTime("");
            String returnData = JSONObject.toJSONString(return_tpVO, SerializerFeature.WriteMapNullValue);
            logger.info("方法" + "deviceInit" + "设备编号为" + tpVO.getDeviceNo() + "的returnData:" + returnData);
            NettyHandlerUtil.appendTagAndWriteAndFlush((SocketChannel) context.channel(), returnData);
        }


    }

    /**
     * 记录招财猫连接日志
     *
     * @param tpVO
     */
    private void recordConnRecord(TransmissionParameterVO tpVO) {
        String deviceNo = tpVO.getDeviceNo();//设备编号
        JSONObject data = tpVO.getData();
        String fwVersion = data.getString("fwVersion");//固件版本
        String type = data.getString("type");//设备类型
        String ccid = data.getString("ccid");//sim卡号
        String signalQuality = data.getString("signalQuality");//信号强度
        String brokenNetworkReconnection = data.getString("brokenNetworkReconnection");//是否断线重连、0不是、1是
        String id = UuidUtil.get32UUID();//主键
        //数据插入machine_cat_connection_record
        String insertsql = "insert into  machine_cat_connection_record (id,device_no,fwversion,type,ccid,signal_quality" +
                ",broken_network_reconnection,conn_time) values (?,?,?,?,?,?,?,?)";
        int key = DruidJdbcUtils.executeUpdate(insertsql, new Object[]{id,deviceNo,fwVersion,type,ccid,
                signalQuality,brokenNetworkReconnection, new Date()});
        String updatesql = "update machine_cat set ccid = ? where device_no = ?";
         key = DruidJdbcUtils.executeUpdate(updatesql, new Object[]{ccid,deviceNo});
        logger.info("方法" + "recordConnRecord" + "设备编号为" + tpVO.getDeviceNo() + "signal_quality:" + signalQuality + ","
                + "ccid:" + ccid);
    }


    public void updateFirmware(ChannelHandlerContext context, TransmissionParameterVO tpVO) {

//		{"data":{"firmareVersion":"52200306", "pkgno":"1","pkgsize":"512"},"deviceNo":"201803250001","method":"updateFirmware", "time":""}
        JSONObject data = tpVO.getData();
        String firmareVersion = data.getString("fwVersion");
        String updateFirmwareFolderPath = SingletonProperty.getInstance().getPropertyValue("systemConfig.updateServer.updateFirmware.folder");
        byte[] bytes = null;
        try {
            bytes = FileUtil.toByteArray(updateFirmwareFolderPath + firmareVersion + ".bin");
        } catch (IOException e) {
            logger.error("IOException!", e);
        }
        Integer pkgno = Integer.valueOf(data.getString("pkgno"));
        Integer pkgsize = Integer.valueOf(data.getString("pkgsize"));
        //如果为第一包，偏移量为（1-0）*512，为0
        int offset = (pkgno - 1) * pkgsize;


        byte[] readData = null;
        //是否为最后一包
        boolean flag_lastPk = false;


        int length = pkgsize;
        int total = bytes.length;
        int minusValue = total - offset;
        //如果从偏移量开始到末尾的长度（minusValue）大于每次需要获取的长度（length），那么按获取的长度截取。
        if (minusValue > length) {
            readData = new byte[length];
            System.arraycopy(bytes,
                    offset,
                    readData,
                    0,
                    length);
            flag_lastPk = false;
        } else {//如果从偏移量开始到末尾的长度（minusValue）小于或者等于每次需要获取的长度（length），那么按minusValue截取。
            readData = new byte[minusValue];
            System.arraycopy(bytes,
                    offset,
                    readData,
                    0,
                    minusValue);
            flag_lastPk = true;
        }


//		byte[] readData = ToolUtil.getSectionBytes(bytes,offset,pkgsize);
        byte[] encoded_byte_readData = Base64.getEncoder().encode(readData);
        String String_encoded_byte_readData = null;
        try {
            String_encoded_byte_readData = new String(encoded_byte_readData, "utf-8");
        } catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
            logger.error("UnsupportedEncodingException!", e);
        }

        JSONObject updateFirmwareDataObject = new JSONObject(true);//设置为true，以put的顺序排列
        updateFirmwareDataObject.put("fwVersion", data.getString("fwVersion"));
        updateFirmwareDataObject.put("pkgno", data.getString("pkgno"));

        if (flag_lastPk) {//如果是最后一包，把"pkgno"设置为"0"，并加入filemd5sum
            updateFirmwareDataObject.put("pkgno", "0");
            updateFirmwareDataObject.put("filemd5sum", MD5Util.getMD5Uppercase(bytes));
        } else {
            updateFirmwareDataObject.put("pkgno", data.getString("pkgno"));
        }
        updateFirmwareDataObject.put("firmware", String_encoded_byte_readData);
        updateFirmwareDataObject.put("datalen", String.valueOf(readData.length));
        updateFirmwareDataObject.put("md5sum", MD5Util.getMD5Uppercase(readData));


//		TransmissionParameterVO return_tpVO = new TransmissionParameterVO();
//    	return_tpVO.setData(updateFirmwareDataObject);
//    	return_tpVO.setDeviceNo(tpVO.getDeviceNo());
//    	return_tpVO.setMethod("return_updateFirmware");
//    	return_tpVO.setTime("");


        //这里单独处理下，把return_updateFirmware这个方法的返回json，"method"节点的数据放到最前面，方便硬件猫判断每包的数据情况。
        JSONObject returnObject = new JSONObject(true);//设置为true，以put的顺序排列
        returnObject.put("method", "return_updateFirmware");
        returnObject.put("data", updateFirmwareDataObject);
        returnObject.put("deviceNo", tpVO.getDeviceNo());
        returnObject.put("time", "");


        String returnData = JSONObject.toJSONString(returnObject, SerializerFeature.WriteMapNullValue);
        logger.info("方法" + "updateFirmware" + "设备编号为" + tpVO.getDeviceNo() + "的returnData:" + returnData);
        NettyHandlerUtil.appendTagAndWriteAndFlush((SocketChannel) context.channel(), returnData);

    }


    public String getUpdateServerAddress(TransmissionParameterVO tpVO) {
        String url = SingletonProperty.getInstance().getPropertyValue("systemConfig.updateServer.serverAddress_" + "1");
        return url;
    }

    public String getConnectionServerAddress(TransmissionParameterVO tpVO) {
        String url = SingletonProperty.getInstance().getPropertyValue("systemConfig.connectionServer.serverAddress_" + "1");
        return url;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//	        System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
        logger.info("---" + ctx.channel().remoteAddress() + " is active---");
    }

    //连接断开时调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("---" + ctx.channel().remoteAddress() + " is inactive!!!---");
        ctx.fireChannelInactive();
    }

}