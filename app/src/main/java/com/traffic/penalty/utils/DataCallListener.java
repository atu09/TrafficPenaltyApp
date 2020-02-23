package com.traffic.penalty.utils;

import org.json.JSONObject;

/**
 * Created by MTAJ-08 on 12/5/2016.
 */
public interface DataCallListener {
      void OnData(JSONObject jsonObject, String tag);
}
