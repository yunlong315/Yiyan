package yiyan.research.model.producer;


import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Producer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    //同步方法
    public Map<String,Object> syncSend(Map<String,Object> map) {
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("userTopic",map,Map.class);
        return  o;
    }
}