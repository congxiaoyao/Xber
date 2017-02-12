package com.congxiaoyao.location.controller;

import com.congxiaoyao.location.manager.GpsSampleCache;
import com.congxiaoyao.location.manager.IGpsSampleCache;
import com.congxiaoyao.location.pojo.GpsSampleRspOuterClass;
import com.congxiaoyao.location.service.def.LocationService;
import com.congxiaoyao.utils.GPSEncoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static com.congxiaoyao.location.pojo.GpsSampleOuterClass.GpsSample;
/**
 * Created by Jaycejia on 2017/2/11.
 */
@RestController
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationService locationService;
    @Resource(name = "gpsSampleCacheProxy")
    private IGpsSampleCache gpsSampleCache;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @RequestMapping(value = "/sendTest", method = RequestMethod.GET)
    public String sendTest() {
        logger.info("invoked");
        GpsSample.Builder builder = GpsSample.newBuilder();
        builder.setLat(100);
        builder.setLng(100);
        builder.setCarId(20136213);
        builder.setVlat(1);
        builder.setVlng(1);
        builder.setTime(System.currentTimeMillis());
        GpsSample gpsSample = builder.build();
        byte[] encode = GPSEncoderUtils.encode(gpsSample.toByteArray());
        simpMessagingTemplate.convertAndSend("/topic/gps", new String(encode));
        return "successfully invoked, sent: " + Arrays.toString(encode) + " length: " + encode.length;
    }

    @RequestMapping("/enTest")
    public void serializeTest() throws Exception{
        List<GpsSample> gpsSampleList = locationService.getSamples();
        long begin = System.currentTimeMillis();
        for (GpsSample sample : gpsSampleList) {
//            byte[] encode = GPSEncoderUtils.encode(sample.toByteArray());
//            byte[] decode = GPSEncoderUtils.decode(encode);
//            GpsSample gpsSample = GpsSample.parseFrom(decode);
//            if (!gpsSample.equals(sample)) {
//                logger.info("ERROR");
//            }
//            gpsSampleCache.put(sample);
            GpsSampleRspOuterClass.GpsSampleRsp.Builder builder = GpsSampleRspOuterClass.GpsSampleRsp.newBuilder();
            GpsSampleRspOuterClass.GpsSampleRsp sampleRsp = builder.setCarId(sample.getCarId())
                    .setLat(sample.getLat())
                    .setLng(sample.getLng())
                    .setVlat(sample.getVlat())
                    .setVlng(sample.getVlng())
                    .setTime(sample.getTime())
                    .setQueryId(123).build();
            logger.info("length: " + sampleRsp.toByteArray().length);
        }
        logger.info("====finished in {} ms====", System.currentTimeMillis() - begin);
    }

}
