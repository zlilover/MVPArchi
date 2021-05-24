package com.zli.mvpexample.util;


import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhen on 2021/3/17.
 */
public class JsonUtils {

    public static <T> List<T> jsonToList(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 转成bean
     *
     * @return
     */
    public static String toString(Object object) {
        String t = null;
        try {
            t = JSON.toJSONString(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
