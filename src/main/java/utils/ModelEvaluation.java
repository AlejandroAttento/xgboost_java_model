package utils;

import dataModels.ProcessedData;
import dataModels.RawDatasource;
import dataModels.ResultData;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoostError;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class ModelEvaluation {
    private Booster model;
    private String libsvmPathTrain;
    private String libsvmPathTest;
    private Dictionary datasets;
    private String configPath;
    private String encoderPath;

    public ModelEvaluation(Booster model, String libsvmPathTrain, String libsvmPathTest, Dictionary datasets,
                           String configPath, String encoderPath) {
        this.model = model;
        this.libsvmPathTrain = libsvmPathTrain;
        this.libsvmPathTest = libsvmPathTest;
        this.datasets = datasets;
        this.configPath = configPath;
        this.encoderPath = encoderPath;

    }

    public void report()
            throws XGBoostError, IOException, ParseException {

        Dictionary results = modelResults(this.model, this.libsvmPathTrain, this.libsvmPathTest,
                (ArrayList<RawDatasource>) this.datasets.get("train"), (ArrayList<RawDatasource>)
                        this.datasets.get("test"));

        JSONObject jsonObject = new JsonReader().read(this.configPath);
        Integer nClasses = Integer.parseInt(jsonObject.get("num_class").toString());
        double defaultProb = 1.0 / Float.valueOf(nClasses);

        ArrayList<ProcessedData> trainResults = (ArrayList<ProcessedData>) results.get("train");
        ArrayList<ProcessedData> testResults = (ArrayList<ProcessedData>) results.get("test");

        analysis(resultsEval(trainResults), "TRAIN", defaultProb, encoderPath);
        analysis(resultsEval(testResults), "TEST", defaultProb, encoderPath);

    }

    private static ArrayList<ProcessedData> addResults(float[][] predictions, ArrayList<RawDatasource> data,
                                                      String context) {

        ArrayList<ProcessedData> results = new ArrayList<>();

        for (int i = 0; i < predictions.length; i++) {

            String actualBeanClass = data.get(i).beanClass;
            String predictedBeanClass = String.valueOf((int) predictions[i][0]);

            ProcessedData pred = new ProcessedData(actualBeanClass, predictedBeanClass,
                    actualBeanClass.equals(predictedBeanClass), context);

            results.add(pred);

        }

        return results;
    }

    private static Dictionary modelResults(Booster model, String trainDataPath, String testDataPath,
                                   ArrayList<RawDatasource> trainData ,
                                   ArrayList<RawDatasource> testData)
            throws XGBoostError, IOException, ParseException {

        Dictionary predictionData = new Hashtable();

        float[][] trainPredictions = model.predict(new DMatrix(trainDataPath));
        float[][] testPredictions = model.predict(new DMatrix(testDataPath));


        predictionData.put("train", addResults(trainPredictions, trainData, "train"));
        predictionData.put("test", addResults(testPredictions, testData, "test"));

        return predictionData;

    }

    private ArrayList<ResultData> resultsEval(ArrayList<ProcessedData> results) {

        ArrayList<ResultData> performance = new ArrayList<>();

        Dictionary truePredictions = new Hashtable();
        Dictionary falsePredictions = new Hashtable();
        Integer rightPredQty = 0;

        for (ProcessedData row : results) {
            if ((truePredictions.get(row.beanClass) == null) || (falsePredictions.get(row.beanClass) == null)) {
                truePredictions.put(row.beanClass, 0);
                falsePredictions.put(row.beanClass, 0);
            }

            if (row.predictionIndicator == true) {
                truePredictions.put(row.beanClass, (Integer) truePredictions.get(row.beanClass) + 1);
            } else {
                falsePredictions.put(row.beanClass, (Integer) falsePredictions.get(row.beanClass) + 1);
            }

        }

        Enumeration<String> beanClasses = truePredictions.keys();

        while (beanClasses.hasMoreElements()) {
            String beanClass = beanClasses.nextElement();

            ResultData resultData = new ResultData(
                    beanClass,
                    (Integer) truePredictions.get(beanClass),
                    (Integer) falsePredictions.get(beanClass)
            );

            performance.add(resultData);
        }

        return performance;
    }

    private void analysis(ArrayList<ResultData> results, String context, Double defaultProb, String configPath) throws IOException, ParseException {

        final DecimalFormat df = new DecimalFormat("0.00");
        JSONObject jsonObject = new JsonReader().read(configPath);

        System.out.println(">>>>>>>>>" + context + " RESULTS<<<<<<<<<<");

        Integer trueTotalPredictions = 0;
        Integer falseTotalPredictions = 0;

        for (ResultData result : results) {

            String beanClass = result.beanClass;
            Integer truePredictions = result.truePredictions;
            Integer falsePredictions = result.falsePredictions;
            String accuracy = df.format((Double.valueOf(truePredictions) / (Double.valueOf(falsePredictions) + Double.valueOf(truePredictions))) * 100);

            System.out.println("\tBean class: " + jsonObject.get(beanClass).toString() + " Accuracy: " + accuracy + "% (" + truePredictions + "/" + falsePredictions + ")");

            trueTotalPredictions += truePredictions;
            falseTotalPredictions += falsePredictions;
        }

        String totalAccuracy = df.format((Double.valueOf(trueTotalPredictions) / (Double.valueOf(falseTotalPredictions) + Double.valueOf(trueTotalPredictions))) * 100);
        System.out.println("\nGeneral Accuracy: " + totalAccuracy + "% (" + trueTotalPredictions + "/" + falseTotalPredictions + ")");

        String randomAccuracy =  df.format(defaultProb*100);
        System.out.println("Default Probability: " + randomAccuracy + "%");

    }

}
