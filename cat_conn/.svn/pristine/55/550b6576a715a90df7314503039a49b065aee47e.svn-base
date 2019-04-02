package com.cgltech.cat_conn.task;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cgltech.cat_conn.dal.entity.TicketCutDetails;
import com.cgltech.cat_conn.server.cat.CatServerManager;
import com.cgltech.cat_conn.server.cat.TransmissionParameterVO;
import com.cgltech.cat_conn.service.TicketCutDetailsService;
import org.springframework.beans.factory.annotation.Value;

/**
 * 发送切票命令定时任务
 */
public class SendCutTicketHandle {
    private static final Logger logger = LoggerFactory.getLogger(SendCutTicketHandle.class);

    private static final int THREAD_POOL_CORE_POOL_SIZE = 4;
	private static final int THREAD_POOL_MAXIMUM_POOL_SIZE = 600;
	private static final int THREAD_POOL_KEEP_ALIVE_TIME = 20;

	private static final BlockingQueue<Runnable> THREAD_POOL_QUEUE = new ArrayBlockingQueue<>(600, true);
	
	private static final ThreadPoolExecutor executors = new ThreadPoolExecutor(
			THREAD_POOL_CORE_POOL_SIZE,
			THREAD_POOL_MAXIMUM_POOL_SIZE,
			THREAD_POOL_KEEP_ALIVE_TIME,
			TimeUnit.SECONDS,
			THREAD_POOL_QUEUE);

    @Autowired
    private TicketCutDetailsService ticketCutDetailsService;

    @Value("${systemConfig.cutTicket.accordToLength}")
    private Integer isAccordToLength;


    public void run() {
        List<TicketCutDetails> list = ticketCutDetailsService.selectTicketCutDetails();

        if (list != null && list.size() != 0) {
            for (TicketCutDetails ticketCutDetails : list) {

                String deviceNo = ticketCutDetails.getDeviceNo();

                if (CatServerManager.INSTANCE.isOnline(deviceNo)) {

                	executors.execute(() -> {

                        //请求明细表status从0-->1,即从初始变为出票中
                        int resultNum = ticketCutDetailsService.updateStatusInitToCuting(ticketCutDetails.getId());
                        if (resultNum > 0) {
                            sendCutTicket(ticketCutDetails);
                        }

                    });

                } else {
                    ticketCutDetailsService.updateStatusInitToFailed(ticketCutDetails.getId());
                }
            }
        }
    }

    public void sendCutTicket(TicketCutDetails ticketCutDetails) {

        JSONObject data = new JSONObject();
        data.put("requestSaleNum", ticketCutDetails.getRequestSaleNum().toString());                //出票张数
        //是否支持按票长出票：0，不支持，1，支持
        if (isAccordToLength == 1) {
            data.put("ticketLength", ticketCutDetails.getTicketLength());                //票长
        }
        data.put("orderNo", ticketCutDetails.getId());

        TransmissionParameterVO tp = new TransmissionParameterVO();
        tp.setData(data);
        tp.setDeviceNo(ticketCutDetails.getDeviceNo());
        tp.setMethod("cutTicket");

        String sendData = JSONObject.toJSONString(tp, SerializerFeature.WriteMapNullValue);

        logger.info("Task-发送切票订单[orderNo:{}]到[deviceNo:{}]开始：订单[{}]",
                ticketCutDetails.getOrderNo(), ticketCutDetails.getDeviceNo(), sendData);

        boolean sendResult = CatServerManager.INSTANCE.sendDataTo(sendData, ticketCutDetails.getDeviceNo(),
                (future) -> {
                    if (!future.isSuccess()) {
                        logger.error("Task-发送切票订单[orderNo:{}]到[deviceNo:{}]失败: 订单[{}]",
                                ticketCutDetails.getOrderNo(),
                                ticketCutDetails.getDeviceNo(),
                                ticketCutDetails);

                        //通讯失败，修改状态位出票失败
                        ticketCutDetailsService.updateStatusCutingToFailed(ticketCutDetails.getId());
                    }
                });
        if (!sendResult) {
            ticketCutDetailsService.updateStatusCutingToInit(ticketCutDetails.getId());
        }
    }
}