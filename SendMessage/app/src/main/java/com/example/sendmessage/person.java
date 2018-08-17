package com.example.sendmessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 帅郑 on 2018/8/9.
 */

public class Person {
    private String number;
    private String name;
    private String phoneNumber;
    private String content;
    public List<String> replaceList;

    public Person(List<String> people){
        this.number = people.get(0);
        this.name = people.get(1);
        this.phoneNumber = people.get(2);
        this.content = people.get(3);
        replaceList = new ArrayList<>();
        for (int i = 4 ; i < people.size(); i++){
            replaceList.add(people.get(i));
        }
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
