package com.congxiaoyao.websocket.bean;

import com.congxiaoyao.location.cache.IGpsSampleCache;
import com.congxiaoyao.location.pojo.CarsQueryMessage;
import com.congxiaoyao.location.pojo.NearestNCarsQueryMessage;
import com.congxiaoyao.util.GPSEncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
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
                "/sysInfo", "hello, " + userId);
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
    @MessageMapping("/trace/nearestNCars")
    public void getCarsPresent(NearestNCarsQueryMessage queryMessage) {
        logger.info("Cars Request Received: {}", queryMessage);
        List<GpsSample[]> carsList = gpsSampleCache.nearestN(queryMessage.getLongitude(), queryMessage.getLongitude(),
                queryMessage.getNumber(), queryMessage.getRadius());
        simpMessagingTemplate.setMessageConverter(new ByteArrayMessageConverter());
        for (GpsSample[] samples : carsList) {
            byte[] payload = mergeGpsSamples(samples, queryMessage.getQueryId());
            simpMessagingTemplate.convertAndSendToUser(queryMessage.getUserId().toString(),
                    "/trace/nearestNCars", payload);
            logger.info("Response for user:{} for query:{}", queryMessage.getUserId()
                    , queryMessage.getQueryId());
            logger.info("Payload length: {}", payload.length);
        }
    }

    /**
     * 根据车辆id查询车辆轨迹
     * @param queryMessage
     */
    @MessageMapping("/trace/cars")
    public void getCarsByIds(CarsQueryMessage queryMessage) {
        logger.info("Cars Request Received: {}", queryMessage);
        List<GpsSample[]> traces = gpsSampleCache.getTraceByCarIds(queryMessage.getCarIds());
        simpMessagingTemplate.setMessageConverter(new ByteArrayMessageConverter());
        for (GpsSample[] samples : traces) {
            byte[] payload = mergeGpsSamples(samples, queryMessage.getQueryId());
            simpMessagingTemplate.convertAndSendToUser(queryMessage.getUserId().toString(),
                    "/trace/cars", payload);
            logger.info("Response for user:{} for query:{}", queryMessage.getUserId()
                    , queryMessage.getQueryId());
            logger.info("Payload length: {}", payload.length);
        }
    }

    /**
     * 对单个车辆的路径包装成一个byte数组 每个点之间用字符','隔开
     *
     * @param samples
     * @param queryId
     * @return
     */
    private byte[] mergeGpsSamples(GpsSample[] samples, long queryId) {
        int length = samples.length;
        if (samples == null || length == 0) {
            if (logger.isDebugEnabled()) {
                logger.warn("no GpsSample data to merge");
            }
            return new byte[0];
        }
        byte[][] byteMatrix = new byte[length][];
        int resLen = length - 1;
        for (int i = 0; i < length; i++) {
            GpsSampleRsp.Builder builder = GpsSampleRsp.newBuilder();
            GpsSample gpsSample = samples[i];
            GpsSampleRsp sampleRsp = builder.setCarId(gpsSample.getCarId())
                    .setLat(gpsSample.getLat())
                    .setLng(gpsSample.getLng())
                    .setVlat(gpsSample.getVlat())
                    .setVlng(gpsSample.getVlng())
                    .setTime(gpsSample.getTime())
                    .setQueryId(queryId).build();
            byte[] encode = GPSEncoderUtils.encode(sampleRsp.toByteArray());
            byteMatrix[i] = encode;
            resLen += encode.length;

            if (logger.isDebugEnabled()) {
                logger.debug("item content: " + sampleRsp);
            }
        }
        byte[] result = new byte[resLen];
        byte[] nowData = byteMatrix[0];
        int pointer = 0;
        System.arraycopy(nowData, 0, result, pointer, nowData.length);
        for (int i = 1; i < length; i++) {
            pointer += nowData.length;
            result[pointer++] = ',';
            nowData = byteMatrix[i];
            System.arraycopy(nowData, 0, result, pointer, nowData.length);
        }
        return result;
    }
}
