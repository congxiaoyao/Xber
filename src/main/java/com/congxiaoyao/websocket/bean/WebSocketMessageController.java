package com.congxiaoyao.websocket.bean;

import com.congxiaoyao.location.cache.IGpsSampleCache;
import com.congxiaoyao.location.pojo.CarsQueryMessage;
import com.congxiaoyao.utils.GPSEncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;
import static com.congxiaoyao.location.pojo.GpsSampleRspOuterClass.GpsSampleRsp;

/**
 * Created by Jaycejia on 2017/2/10.
 */
@RestController
public class WebSocketMessageController {
    private final Logger logger = LoggerFactory.getLogger(WebSocketMessageController.class);
    @Resource(name = "gpsSampleCacheProxy")
    private IGpsSampleCache gpsSampleCache;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * echo测试
     *
     * @param message
     * @return
     */
    @MessageMapping("/hello")
    @SendTo("/topic/echo")
    public String echo(String message) {
        logger.info("echo invoked, userId = " + message);
        return "hello, " + message;
    }

    /**
     * echo测试
     *
     * @param userId
     * @return
     */
    @MessageMapping("/helloUser")
    public void echoToUser(String userId) {
        logger.info("echo invoked, userId = " + userId);
        simpMessagingTemplate.convertAndSendToUser(userId,
                "/echo", "hello, " + userId);
    }

    /**
     * 上传定位采样数据
     *
     * @param message
     * @throws Exception
     */
    @MessageMapping("/gpsSample/upload")
    public void uploadSample(byte[] message) throws Exception {
        byte[] decode = GPSEncoderUtils.decode(message);
        GpsSample gpsSample = GpsSample.parseFrom(decode);
        if (logger.isDebugEnabled()) {
            logger.debug("Received gpsSample: " + gpsSample);
        }
        gpsSampleCache.put(gpsSample);
    }

    /**
     * 查询某一点附近的N辆车及运动轨迹
     *
     * @param queryMessage
     */
    @MessageMapping("/nearestNCars")
    public void getCarsPresent(CarsQueryMessage queryMessage) {
        List<GpsSample[]> carsList = gpsSampleCache.nearestN(queryMessage.getLongitude(), queryMessage.getLongitude(),
                queryMessage.getNumber(), queryMessage.getRadius());
        if (logger.isInfoEnabled()) {
            logger.info("Cars Request Received: " + queryMessage);
        }
        for (GpsSample[] samples : carsList) {
            String payload = wrapSamples(samples, queryMessage.getQueryId());
            simpMessagingTemplate.convertAndSendToUser(queryMessage.getUserId().toString(),
                    "/nearestNCars", payload);
            simpMessagingTemplate.convertAndSend("/topic/nearestNCars", payload);
            if (logger.isInfoEnabled()) {
                logger.info("Response for user:" + queryMessage.getUserId()
                        + " for query:" + queryMessage.getQueryId()
                        + " Payload: " + payload);
                logger.info("Payload length: " + payload.getBytes().length);
            }
        }
    }

    /**
     * 对单个车辆的路径包装成一个String
     *
     * @param samples
     * @return
     */
    private String wrapSamples(GpsSample[] samples, long queryId) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < samples.length; i++) {
            GpsSampleRsp.Builder builder = GpsSampleRsp.newBuilder();
            GpsSampleRsp sampleRsp = builder.setCarId(samples[i].getCarId())
                    .setLat(samples[i].getLat())
                    .setLng(samples[i].getLng())
                    .setVlat(samples[i].getVlat())
                    .setVlng(samples[i].getVlng())
                    .setTime(samples[i].getTime())
                    .setQueryId(queryId).build();
            result.append(new String(GPSEncoderUtils.encode(sampleRsp.toByteArray())));
            if (i != samples.length - 1) {
                result.append("II");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("item length: " + sampleRsp.toByteArray().length);
                logger.debug("item content: " + sampleRsp);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("total length: " + result.toString().getBytes().length);
        }
        return result.toString();
    }

}
