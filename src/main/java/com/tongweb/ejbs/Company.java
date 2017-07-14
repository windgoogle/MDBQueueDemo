package com.tongweb.ejbs;

import java.io.Serializable;

/**
 * Created by huangfeng on 2017/7/13.
 */
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    public Company(String name,String address){
        this.name =name;
        this.address=address;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address=address;
    }

}
