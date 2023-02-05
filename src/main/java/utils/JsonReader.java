package utils;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class JsonReader {
    public static JSONObject read(String path) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(path));
        JSONObject jsonObject = (JSONObject)obj;
        return jsonObject;
    }
}
