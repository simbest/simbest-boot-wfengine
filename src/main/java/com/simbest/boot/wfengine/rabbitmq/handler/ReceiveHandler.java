package com.simbest.boot.wfengine.rabbitmq.handler;/**
 * @author Administrator
 * @create 2020/2/18 18:21.
 */

import com.rabbitmq.client.Channel;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.wfengine.rabbitmq.service.IMqReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName ReceiveHandler
 * @Description TODO
 * @Author Administrator
 * @Date 2020/2/18 18:21
 * @Version 1.0
 **/
@Component
@Slf4j
public class ReceiveHandler implements ChannelAwareMessageListener {

    @Autowired
    private IMqReceiveService mqReceiveServiceImpl;

    /**
     * @param 1、处理成功，这种时候用basicAck确认消息； 2、可重试的处理失败，这时候用basicNack将消息重新入列；
     *                                  3、不可重试的处理失败，这时候使用basicNack将消息丢弃。
     *                                  <p>
     *                                  basicNack(long deliveryTag, boolean multiple, boolean requeue)
     *                                  deliveryTag:该消息的index
     *                                  multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
     *                                  requeue：被拒绝的是否重新入队列
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        log.info("接收到消息:" + new String(body));
        try {
            boolean result = mqReceiveServiceImpl.handler(new String(body));
            if(true){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//确认消息消费成功
            }else{
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//消息丢弃
            }

        } catch (Exception e) {
            log.error("接受消息失败："+ Exceptions.getStackTraceAsString(e));
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);//消息丢弃
        }
    }
}
