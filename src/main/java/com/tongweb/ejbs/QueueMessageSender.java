package com.tongweb.ejbs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;

/**
 * Created by huangfeng on 2017/7/13.
 */
@Stateless (mappedName = "ejb/QueueMessageSender")
public class QueueMessageSender implements QueueMessageSenderRemote {
    @Resource(mappedName="jms/queueXAConnectionFactory") private QueueConnectionFactory factory;
    @Resource(mappedName="jms/queue") private Queue queue;


    public void send() {
        QueueConnection conn = null;
        QueueSession session = null;
        try {
            conn = factory.createQueueConnection();
            session = conn.createQueueSession(true, QueueSession.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);

            //Send TextMessage
            TextMessage tmsg = session.createTextMessage("这是发送的TextMessage，用来测试TongWeb");
            producer.send(tmsg);

            //Send OjbectMessage(object must implement Serializable interface)
            producer.send(session.createObjectMessage(new Company("东方通科技", "北京市海淀南路32号中信国安数码港8层")));

            //Send MapMessage
            MapMessage mapmsg = session.createMapMessage();
            mapmsg.setObject("no1", "北京东方通科技");
            producer.send(mapmsg);

            //Send BytesMessage
            BytesMessage bmsg = session.createBytesMessage();
            bmsg.writeBytes("这是发送的BytesMessage，用来测试TongWeb".getBytes());
            producer.send(bmsg);

            //Send StreamMessage
            StreamMessage smsg = session.createStreamMessage();
            smsg.writeString("东方通官网,http://www.tongtech.com");
            producer.send(smsg);

        }catch (Exception e){
            e.printStackTrace();

        }finally{
            try {
                session.close ();
                conn.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
