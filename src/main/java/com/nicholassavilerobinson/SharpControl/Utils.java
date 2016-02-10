package com.nicholassavilerobinson.SharpControl;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static JSONObject getJsonObjectFromFile(String filename) throws IOException {
        File fh = new File(filename);
        String jsonString = FileUtils.readFileToString(fh, "UTF-8");
        return new JSONObject(jsonString);
    }

}
