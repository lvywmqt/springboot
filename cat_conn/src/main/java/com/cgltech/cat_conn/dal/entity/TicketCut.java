package com.cgltech.cat_conn.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 猫通讯记录表映射类
 */
public class TicketCut implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2696990474572762235L;
    /**
     * 主键
     */
    private String id;
    /**
     * 请求切票数量
     */
    private Integer requestSaleNum;

    /**
     * 成功出票数
     */
    private Integer successSaleNum;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 时间戳
     */
    private String timestamp;
    ;
    /**
     * 请求响应状态
     * 0--初始，1--响应成功
     */
    private Integer responseStatus;
    /**
     * 订单状态
     * 0--初始，1--出票成功，2--部分成功，3--出票失败
     */
    private Integer status;

    
    private Integer errcode;
    
    private String notifyUrl;
    
    public String getId() {
        return id;
    }

    public Integer getRequestSaleNum() {
        return requestSaleNum;
    }

    public Integer getSuccessSaleNum() {
        return successSaleNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public String getMethod() {
        return method;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRequestSaleNum(Integer requestSaleNum) {
        this.requestSaleNum = requestSaleNum;
    }

    public void setSuccessSaleNum(Integer successSaleNum) {
        this.successSaleNum = successSaleNum;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"requestSaleNum\":")
                .append(requestSaleNum);
        sb.append(",\"successSaleNum\":")
                .append(successSaleNum);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"deviceNo\":\"")
                .append(deviceNo).append('\"');
        sb.append(",\"method\":\"")
                .append(method).append('\"');
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"updateTime\":\"")
                .append(updateTime).append('\"');
        sb.append(",\"timestamp\":\"")
                .append(timestamp).append('\"');
        sb.append(",\"responseStatus\":")
                .append(responseStatus);
        sb.append(",\"status\":")
                .append(status);
        sb.append(",\"errcode\":")
                .append(errcode);
        sb.append(",\"notifyUrl\":\"")
                .append(notifyUrl).append('\"');
        sb.append('}');
        return sb.toString();
    }
}