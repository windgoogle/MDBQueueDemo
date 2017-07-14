package com.tongweb.client;

import com.tongweb.ejbs.QueueMessageSender;
import com.tongweb.ejbs.QueueMessageSenderRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by huangfeng on 2017/7/13.
 */
public class MDBTestClient {
    protected static int threadNum = 10;

    protected static int requestNum = 1;

    protected static String method;

    protected static Properties props = new Properties();

    public static void main(String[] args) throws  Exception{
        parseCommondLineArgs(args);

        InitialContext ctx = new InitialContext(props);

        String jndiName = props.getProperty("ejb.jndiname");
        QueueMessageSenderRemote remote = (QueueMessageSenderRemote) ctx.lookup(jndiName);


        excute(remote);

    }

    private static void excute(QueueMessageSenderRemote remote) throws InterruptedException{
        long start=System.currentTimeMillis();
        Thread[] threads = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new Thread(new Task(remote));
            threads[i].start();
        }

        System.out.print(method);
        for (int i = 0; i < threadNum; ) {
            if(threads[i].isAlive()){
                Thread.sleep(1000);
                System.out.print(".");
            }else{
                i++;
            }
        }
        long end=System.currentTimeMillis();

        System.out.print("Test completed! spent time: "+(end-start)/1000+"s");

    }

    public static void parseCommondLineArgs(String[] args) throws Exception {
        System.out
                .println("Usage: java com.tongweb.client.EJBTestClient -threadNum <threadNum> -requestNum <requestNum> -method <method>");
        HashMap<String, String> optionsMap = parseCommondLineArgs(Arrays
                .asList(args).listIterator());

        String threadNumVal = optionsMap.get("-threadNum");
        if (threadNumVal == null) {
            System.out.println("-threadNum is not specified!");
        } else {
            threadNum = Integer.parseInt(threadNumVal);
        }

        String requestNumVal = optionsMap.get("-requestNum");
        if (requestNumVal == null) {
            System.out.println("-requestNum is not specified!");
        } else {
            requestNum = Integer.parseInt(requestNumVal);
        }
        method = optionsMap.get("-method");
        if (method == null) {
            System.out.println("-method is not specified!");
        }

        // props.put("jboss.naming.client.ejb.context", true);
        initJndi("jndi.properties", props);
        System.out.println("providerUrl:"
                + props.getProperty(Context.PROVIDER_URL));
        System.out.println("jndiName:" + props.getProperty("ejb.jndiname"));
        System.out.println("threadNum:" + threadNum);
        System.out.println("executeNum:" + requestNum);
        System.out.println("method:" + method);
    }

    public static HashMap<String, String> parseCommondLineArgs(
            Iterator<String> argIterator) {
        HashMap<String, String> optionsMap = new HashMap<String, String>();
        while (argIterator.hasNext()) {
            String option = argIterator.next();
            if (option.startsWith("-")) {
                if (argIterator.hasNext()) {
                    String optionValue = argIterator.next();
                    optionsMap.put(option, optionValue);
                } else {
                    throw new IllegalArgumentException("Argument don't match!");
                }
            } else {
                throw new IllegalArgumentException("Argument don't match!");
            }
        }
        return optionsMap;
    }

    private static void initJndi(String propath, Properties props) {
        try {
            props.load(MDBTestClient.class.getClassLoader()
                    .getResourceAsStream(propath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Task implements Runnable {
        private QueueMessageSenderRemote remote;
        private static int threadCount = 0;
        private String threadName = String.valueOf(threadCount++);

        public Task(QueueMessageSenderRemote remote) {
            this.remote = remote;
            Thread.currentThread().setName("TestThread " + threadName);
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            long start = System.currentTimeMillis();
            try {
                for (int i = 0; requestNum == -1 || i < requestNum; i++) {

                    if ("send".equals(method)) {
                           remote.send();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            long escape = (System.currentTimeMillis() - start)/1000;

            System.out.println("Thread " + threadName + " completed ! spent time : " + escape+"s");
        }
    }
}
