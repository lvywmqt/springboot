package com.cgltech.cat_conn.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 猫通讯记录明细表映射类
 */
public class TicketCutDetails implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2634406978311411873L;
	/**
     * 主键
     */
    private String id;
    /**
     * 猫通讯记录表主键
     */
    private String ticketCutId;
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
     * 设备编号
     */
    private String deviceNo;
    /**
     * 票长
     */
    private String ticketLength;
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
    private String timestamp;;
    /**
     * 请求响应状态
     * 0--初始，1--响应成功
     */
    private Integer cutTicketStatus;
    /**
     * 订单状态
     * 0--初始，1--出票中，2--出票成功,3--出票失败
     */
    private Integer status;
    /**
     * 切票机返回错误码
     */
    private String errcode;

    private Long serialNum;
    
    public String getId() {
        return id;
    }

    public String getTicketCutId() {
        return ticketCutId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getCutTicketStatus() {
        return cutTicketStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTicketCutId(String ticketCutId) {
        this.ticketCutId = ticketCutId;
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

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setCutTicketStatus(Integer cutTicketStatus) {
        this.cutTicketStatus = cutTicketStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
    
	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

    public String getTicketLength() {
        return ticketLength;
    }

    public void setTicketLength(String ticketLength) {
        this.ticketLength = ticketLength;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"ticketCutId\":\"")
                .append(ticketCutId).append('\"');
        sb.append(",\"requestSaleNum\":")
                .append(requestSaleNum);
        sb.append(",\"successSaleNum\":")
                .append(successSaleNum);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"deviceNo\":\"")
                .append(deviceNo).append('\"');
        sb.append(",\"ticketLength\":")
                .append(ticketLength);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append(",\"updateTime\":\"")
                .append(updateTime).append('\"');
        sb.append(",\"timestamp\":\"")
                .append(timestamp).append('\"');
        sb.append(",\"cutTicketStatus\":")
                .append(cutTicketStatus);
        sb.append(",\"status\":")
                .append(status);
        sb.append(",\"errcode\":\"")
                .append(errcode).append('\"');
        sb.append('}');
        return sb.toString();
    }
}