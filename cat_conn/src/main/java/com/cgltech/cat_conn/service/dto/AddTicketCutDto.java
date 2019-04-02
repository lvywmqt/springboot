/**
 * 类名称： AddTicketCutDto.java
 * 类描述：
 *
 * @author Li xiao jun
 * 作者单位： 中竞
 * 联系方式：
 * 修改时间：2018年6月2日
 * @version 2.0
 */
package com.cgltech.cat_conn.service.dto;

/**
 * @author User
 *
 */
public class AddTicketCutDto {

    /**
     * 请求切票数量
     */
    private Integer requestSaleNum;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 请求方法名
     */
    private String method;
    /**
     * 切票票长
     */
    private Integer ticketLength;

    /**
     * 请求方法名
     */
    private String notifyUrl;

    /**
     * 时间戳
     */
    private String timestamp;

    public Integer getRequestSaleNum() {
        return requestSaleNum;
    }

    public void setRequestSaleNum(Integer requestSaleNum) {
        this.requestSaleNum = requestSaleNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTicketLength() {
        return ticketLength;
    }

    public void setTicketLength(Integer ticketLength) {
        this.ticketLength = ticketLength;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"requestSaleNum\":")
                .append(requestSaleNum);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"deviceNo\":\"")
                .append(deviceNo).append('\"');
        sb.append(",\"method\":\"")
                .append(method).append('\"');
        sb.append(",\"ticketLength\":")
                .append(ticketLength);
        sb.append(",\"notifyUrl\":\"")
                .append(notifyUrl).append('\"');
        sb.append(",\"timestamp\":\"")
                .append(timestamp).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
