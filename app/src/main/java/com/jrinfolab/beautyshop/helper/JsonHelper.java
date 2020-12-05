package com.jrinfolab.beautyshop.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    public static final String GROUP_NAME = "group_name";
    public static final String PRICE_LIST = "price_list";
    public static final String SERVICE_NAME = "service_name";
    public static final String SERVICE_PRICE = "service_price";


    public static JSONArray getPriceList() {

        JSONArray array = new JSONArray();

        try {

            // 1ST GROUP
            JSONObject obj11 = new JSONObject();
            obj11.put(SERVICE_NAME, "Hair Cut");
            obj11.put(SERVICE_PRICE, 120);

            JSONObject obj12 = new JSONObject();
            obj12.put(SERVICE_NAME, "Child Hair Cut");
            obj12.put(SERVICE_PRICE, 120);

            JSONArray array1 = new JSONArray();
            array1.put(obj11);
            array1.put(obj12);

            JSONObject obj1 = new JSONObject();
            obj1.put(GROUP_NAME, "Hair");
            obj1.put(PRICE_LIST, array1);

            // 2ND GROUP
            JSONObject obj21 = new JSONObject();
            obj21.put(SERVICE_NAME, "Hair Cut");
            obj21.put(SERVICE_PRICE, 120);

            JSONObject obj22 = new JSONObject();
            obj22.put(SERVICE_NAME, "Child Hair Cut");
            obj22.put(SERVICE_PRICE, 120);

            JSONArray array2 = new JSONArray();
            array1.put(obj21);
            array1.put(obj22);

            JSONObject obj2 = new JSONObject();
            obj2.put(GROUP_NAME, "Color");
            obj2.put(PRICE_LIST, array2);

            array.put(obj1).put(obj2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array;
    }
}
