package com.nicholassavilerobinson.SharpControl;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static JSONObject getJsonObjectFromResource(String resource) throws IOException {
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(resource);
        return new JSONObject(IOUtils.toString(is, "UTF-8"));
    }

}
