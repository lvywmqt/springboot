/**
 * 类名称： UploadStatus.java
 * 类描述：
 *
 * @author Li xiao jun
 * 作者单位： 中竞
 * 联系方式：
 * 修改时间：2018年5月28日
 * @version 2.0
 */
package com.cgltech.cat_conn.http;

import com.cgltech.cat_conn.util.CatSignatureUtil;
import com.cgltech.cat_conn.util.HttpClientCommon;

/**
 * @author User
 *
 */
public class UploadStatus {

	private static String domain = "127.0.0.1:9202";//"47.96.102.216:9202";
    /**
     * @param args
     */
    public static void main(String[] args) {

        getStatus();
        //getVersion();
        //getReboot();
//        cutTicket();
        //getTicketLength();
    }

    /**
     * 重启
     */
    private static void getReboot() {
        String apiUrl = "http://" + domain + "/conf";//中维test
        //String apiUrl = "http://47.104.241.75:9202/conf";//中竞test
        String getReboot = "{\"code\":0,\"data\":{\"data\":null,\"deviceNo\":\"2018050580040\",\"method\":\"reboot\"},\"message\":null}";
        String sign = CatSignatureUtil.sign(getReboot);
        String reqData = sign + getReboot;
        String result = HttpClientCommon.httpPost(apiUrl, reqData);
    }

    /**
     * 获取状态
     */
    private static void getStatus() {
        String apiUrl = "http://" + domain + "/query";//中维test
        //String apiUrl = "http://47.104.241.75:9202/query";//中竞test
        String getStatus = "{\"code\":0,\"data\":{\"data\":null,\"deviceNo\":\"2018050580240\",\"method\":\"getStatus\"},\"message\":null}";
        String sign = CatSignatureUtil.sign(getStatus);
        String reqData = getStatus;
        String result = HttpClientCommon.httpPost(apiUrl, reqData);
    }

    /**
     * 获取版本
     */
    private static void getVersion() {
        String apiUrl = "http://" + domain + "/query";//中维test
        //String apiUrl = "http://47.104.241.75:9202/query";//中竞test
        String getVersion = "{\"code\":0,\"data\":{\"data\":null,\"deviceNo\":\"2018050580985\",\"method\":\"getVersion\"},\"message\":null}";
        String sign = CatSignatureUtil.sign(getVersion);
        String reqData = sign + getVersion;
        String result = HttpClientCommon.httpPost(apiUrl, reqData);
    }

    /**
     * 切票
     */
    private static void cutTicket() {
        String apiUrl = "http://" + domain + "/sale";//中维test
        //String apiUrl = "http://47.104.241.75:9202/sale";//中竞test
        String cutTicket = "{\"code\":0,\"data\":{\"data\":{\"requestSaleNum\":\"1\",\"orderNo\":\"A3101040000120180413140043197\"},\"deviceNo\":\"2018050580040\",\"method\":\"cutTicket_cat_biz\"},\"message\":null}";
        String sign = CatSignatureUtil.sign(cutTicket);
        String reqData = sign + cutTicket;
        String result = HttpClientCommon.httpPost(apiUrl, reqData);
    }

    /**
     * 获取票长
     */
    private static void getTicketLength() {
        String apiUrl = "http://" + domain + "/query";//中维test
        //String apiUrl = "http://47.104.241.75:9202/query";//中竞test\
        String getTicketLength = "{\"code\":0,\"data\":{\"data\":null,\"deviceNo\":\"2018050580040\",\"method\":\"getTicketLength\"},\"message\":null}";
        String sign = CatSignatureUtil.sign(getTicketLength);
        String reqData = sign + getTicketLength;
        String result = HttpClientCommon.httpPost(apiUrl, reqData);
    }

}
