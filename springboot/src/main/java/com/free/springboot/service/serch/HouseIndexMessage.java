package com.free.springboot.service.serch;

public class HouseIndexMessage {

	public static final String INDEX = "index";
	public static final String REMOVE = "remove";
	
	public static final int MAX_RETRY = 3;
	
	private Long HouseId;
	private String operation;
	private int retry = 0;
	
	public Long getHouseId() {
		return HouseId;
	}
	public void setHouseId(Long houseId) {
		HouseId = houseId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	
	public HouseIndexMessage(Long houseId, String operation, int retry) {
		super();
		HouseId = houseId;
		this.operation = operation;
		this.retry = retry;
	}
	public HouseIndexMessage() {
		super();
	}
	
}
