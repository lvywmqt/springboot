/**
 * 
* 类名称： IHandleCatServer.java
* 类描述： 
* @author Li xiao jun
* 作者单位： 中竞
* 联系方式：
* 修改时间：2018年4月13日
* @version 2.0
 */
package com.cgltech.cat_conn.server.cat.handle;

import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author User
 *
 */
public interface IHandleCatServer {
	public String handle(ChannelHandlerContext context, TransmissionParameterVO transmissionParameterVO);
}
