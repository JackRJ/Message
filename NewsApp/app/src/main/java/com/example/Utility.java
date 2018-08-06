package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 帅郑 on 2018/6/2.
 */

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        NewsList newsList = new NewsList();
        try {
            JSONObject jsonObject = new JSONObject(requestText);
            newsList.msg = jsonObject.getString("reason");
            newsList.result = jsonObject.getString("result");
            newsList.data = new JSONObject(newsList.result).getString("data");
            newsList.mnewsList = gson.fromJson(newsList.data,new TypeToken<List<New>>(){}.getType());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
