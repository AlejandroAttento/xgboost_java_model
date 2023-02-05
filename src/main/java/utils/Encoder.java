package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataModels.RawDatasource;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class Encoder {
    public ArrayList<RawDatasource> data;
    private String path;

    public Encoder (ArrayList<RawDatasource> data, String path) {
        this.data = data;
        this.path = path;
    }

    public ArrayList<RawDatasource> encode()
            throws FileNotFoundException, JsonProcessingException {

        Dictionary codes = createEncoderHashtable(this.data, this.path);

        for (RawDatasource row : this.data) {
            row.beanClass = codes.get(row.beanClass).toString();
        }

        return this.data;

    }

    private Dictionary createEncoderHashtable(ArrayList<RawDatasource> data, String path)
            throws FileNotFoundException, JsonProcessingException {

        Integer encoded_num = 0;
        Dictionary codes = new Hashtable();

        for (RawDatasource row : data) {

            if (codes.get(row.beanClass) == null) {
                codes.put(row.beanClass, encoded_num.toString());
                encoded_num++;
            }
        }

        Dictionary invCodes = invertDictionary(codes);

        ObjectMapper mapper = new ObjectMapper();

        String codesJson = mapper.writeValueAsString(invCodes);

        PrintWriter out = new PrintWriter(path);
        out.println(codesJson);
        out.close();

        return codes;

    }

    private static Dictionary invertDictionary(Dictionary dict) {

        Dictionary invDict = new Hashtable<>();

        Enumeration<String> keys = dict.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();

            invDict.put(dict.get(key).toString(), key);
        }

            return invDict;
    }


}
