package org.song.demo.CommonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class GsonUtil<T> {
    private static Gson gson;


    public GsonUtil() {

    }

    public static void getJson() {

    }


    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder gb = new GsonBuilder()
                    .serializeNulls() ;
            gson = gb.create();
            //gson 非空统一解决
        }
        return gson;
    }


//    public T formateJson2Bean(String json, Class cls) {
//
//
//        return (T) getGson().fromJson(json, cls);
//
//    }

    public static <E> E formateJson2Bean(String json, Class<E> cls) {

//        System.out.println(t.getClass());


//        return (E) getGson().fromJson(json, cls);
        return getGson().fromJson(json, cls);
    }




    public static <E> E formateJson2List(String json, Class<E> cls) {

//        System.out.println(t.getClass());

        Type beanType = new TypeToken<List<E>>() {
        }.getType();

//        return (E) getGson().fromJson(json, cls);
        return getGson().fromJson(json, beanType);
    }


    public static <E> E formateJson2Bean(String json, Type beanType) {

//        System.out.println(t.getClass());
//        return (E) getGson().fromJson(json, cls);
        return (E) getGson().fromJson(json, beanType);
    }





    public static String Bean2Json(Object object) {
        String json = "";
//        System.out.println(t.getClass());
        json = getGson().toJson(object);
        return json;
    }


    public static String formatJson2String(String json) {

        int level = 0;
        //存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int index = 0; index < json.length(); index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = json.charAt(index);
//          System.out.println(s.charAt(index));

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
//                System.out.println("123"+jsonForMatStr);
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();
    }


    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }


}
