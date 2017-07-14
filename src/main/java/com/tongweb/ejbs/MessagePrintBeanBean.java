package com.tongweb.ejbs;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.io.ByteArrayOutputStream;

/**
 * Created by huangfeng on 2017/7/13.
 */
@MessageDriven(mappedName = "jms/queue", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class MessagePrintBeanBean implements MessageListener {
    public void ejbCreate() {
        System.out.println("enter ejbCreate method !");
    }


    public void onMessage(Message msg) {
        try{
            if(msg instanceof TextMessage){
                TextMessage tmsg=(TextMessage)msg;
                String content=tmsg.getText();
                System.out.println("TextMessage: "+content);

            }else if(msg instanceof ObjectMessage){
                ObjectMessage omsg=(ObjectMessage)msg;
                Company company=(Company)omsg.getObject();
                String content=company.getName()+" address "+company.getAddress();
                System.out.println("ObjectMessage: "+content);

            }else if(msg instanceof MapMessage){
                MapMessage map=(MapMessage)msg;
                String content=map.getString("no1");
                System.out.println("MapMessage: "+content);

            }else if(msg instanceof BytesMessage){
                BytesMessage bmsg=(BytesMessage)msg;
                ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
                byte[] buffer=new byte[256];
                int length=0;
                while((length=bmsg.readBytes(buffer))!=-1){
                    byteStream.write(buffer,0,length);
                }
                String content=new String(byteStream.toByteArray());
                byteStream.close();
                System.out.println("BytesMessage: "+content);

            }else if(msg instanceof StreamMessage){
                StreamMessage smsg=(StreamMessage)msg;
                String content=smsg.readString();
                System.out.println("StreamMessage: "+content);
            }

        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
