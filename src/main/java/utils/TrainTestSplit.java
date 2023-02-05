package utils;

import dataModels.RawDatasource;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class TrainTestSplit {

    private Double testSize;
    private ArrayList<RawDatasource> data;
    public Dictionary datasets;

    public TrainTestSplit(Double testSize, ArrayList<RawDatasource> data) {
        this.testSize = testSize;
        this.data = data;
        this.datasets = new Hashtable();
    }

    public Dictionary split() {

        Random rand = new Random();

        ArrayList<RawDatasource> train = new ArrayList<>();
        ArrayList<RawDatasource> test = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (rand.nextFloat() <= this.testSize) {
                test.add(this.data.get(i));
            }
            else {
                train.add(this.data.get(i));
            }

        }

        this.datasets.put("train", train);
        this.datasets.put("test", test);

        return this.datasets;

    }

}
