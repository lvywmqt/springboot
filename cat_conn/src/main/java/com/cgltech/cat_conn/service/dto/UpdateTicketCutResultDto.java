package com.cgltech.cat_conn.service.dto;

/**
 * 猫通讯记录明细表映射类
 */
public class UpdateTicketCutResultDto {

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
     * 时间戳
     */
    private String timestamp;

    private String errcode;
    
    public Integer getRequestSaleNum() {
		return requestSaleNum;
	}
	public void setRequestSaleNum(Integer requestSaleNum) {
		this.requestSaleNum = requestSaleNum;
	}
	
	public Integer getSuccessSaleNum() {
		return successSaleNum;
	}
	public void setSuccessSaleNum(Integer successSaleNum) {
		this.successSaleNum = successSaleNum;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateTicketCutResultDto [requestSaleNum=");
		builder.append(requestSaleNum);
		builder.append(", successSaleNum=");
		builder.append(successSaleNum);
		builder.append(", orderNo=");
		builder.append(orderNo);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", errcode=");
		builder.append(errcode);
		builder.append("]");
		return builder.toString();
	}
	
	
}