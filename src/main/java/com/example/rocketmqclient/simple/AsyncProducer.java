package com.example.rocketmqclient.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;

/**
 * @author Boris
 * @date 2020/5/29 8:51
 */
public class AsyncProducer {

    private static CountDownLatch countDownLatch = new CountDownLatch(3);
    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("10.0.92.150:9876");
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
//        Message syncMsg = new Message("TopicTest" , "TagA" , ("Hello RocketMQ,this is sync msg").getBytes(RemotingHelper.DEFAULT_CHARSET));
//        SendResult sendResult = producer.send(syncMsg);
//        System.out.printf("%s%n", sendResult);
        for (int i = 0; i < 3; i++) {
            final int index = i;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest", "TagA", "OrderID188", "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        countDownLatch.await();
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
