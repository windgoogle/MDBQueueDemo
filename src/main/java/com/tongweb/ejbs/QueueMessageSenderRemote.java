package com.tongweb.ejbs;

import javax.ejb.Remote;
import java.rmi.RemoteException;

/**
 * Created by huangfeng on 2017/7/13.
 */
@Remote
public interface QueueMessageSenderRemote {

    public void send();
}
