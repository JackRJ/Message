package com.example;

import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 帅郑 on 2018/6/2.
 */

public class NewsList extends JSONObject {

    String stat;

    String msg ;

    String result;

    String data;

    @SerializedName("newslist")
    public List<New> mnewsList;

}
