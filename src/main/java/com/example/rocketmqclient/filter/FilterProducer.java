package com.example.rocketmqclient.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author Boris
 * @date 2020/6/1 13:44
 */
public class FilterProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("FilterProducerGroup");
        producer.setNamesrvAddr("10.0.92.150:9876");
        producer.start();

        String tag = "filtertag";
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("FilterTopicTest", tag, ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // Set some properties.
            msg.putUserProperty("a", String.valueOf(i));
            SendResult sendResult = producer.send(msg);
        }

        producer.shutdown();
    }
}
