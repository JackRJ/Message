package com.example.sendmessage;

/**
 * Created by 帅郑 on 2018/8/9.
 */

public class Person {
    private String number;
    private String name;
    private String phoneNumber;
    private String content;

    public Person(String number, String name, String phoneNumber, String content){
        this.number = number;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.content = content;
    }

    @Override
    public String toString(){
        return number+name+phoneNumber+content;
    }

    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getContent(){
        return content;
    }
}
