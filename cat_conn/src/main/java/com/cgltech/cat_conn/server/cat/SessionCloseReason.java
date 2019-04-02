package com.cgltech.cat_conn.server.cat;
public enum SessionCloseReason {
	
	/** 正常退出 */
	NORMAL,
	/** 读超时 */
	READ_OVER_TIME,
	/** 断开连接 */
	DISCONNECT,
	/** 重新注册 */
	RE_REGISTER,
}