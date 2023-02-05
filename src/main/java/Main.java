import dataModels.RawDatasource;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.XGBoostError;
import mlModels.BasicXGBoostModel;
import org.json.simple.parser.ParseException;
import utils.*;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;

public class Main {
    public static void main(String[] args) throws IOException, IntrospectionException, InvocationTargetException, IllegalAccessException, ParseException, XGBoostError, InterruptedException {

        Path datasetPath = Paths.get(args[0]);
        Boolean verbose = Boolean.parseBoolean(args[1]);

        if (verbose) {
            System.out.println("Process started.");
        }

        File zipFile = new File(datasetPath.toUri());

        Path resultingPath = Paths.get(datasetPath.toString().substring(0, datasetPath.toString().lastIndexOf('.')));
        File resultingDir = new File(resultingPath.toUri());

        Path targetPath = Paths.get(datasetPath.getParent().toString());
        File targetDir  = new File(targetPath.toUri());

        // Checks if the file is a zip file, if it exists and if the target path is already a directory.
        if (datasetPath.toString().endsWith(".zip") && zipFile.exists() && !resultingDir.isDirectory()) {

            if (verbose) {
                System.out.println("Unzipping data source.");
            }

            new Unzip(datasetPath.toString(), targetPath.toString()).unzip();

        }

        // Read the extracted Excel file
        if (verbose) {
            System.out.println("Reading Excel file.");
        }

        ArrayList<RawDatasource> data = new Reader(resultingPath.toString() + "/Dry_Bean_Dataset.xlsx").fromExcel();

        // Verifying if we have null values, those rows will be removed.
        if (verbose) {
            System.out.println("Verifying Null values.");
        }

        data = new DataChecks(data).nullChecks();

        // Encoding the labels into numerical values.
        if (verbose) {
            System.out.println("Encoding data.");
        }

        String encodedPath = targetPath.getParent().toString() + "/encoded.json";
        data = new Encoder(data, encodedPath).encode();



        // Splitting the dataset into training and testing data.
        if (verbose) {
            System.out.println("Splitting dataset");
        }

        Dictionary datasets = new TrainTestSplit(0.2, data).split();

        // Transforms the data into LibSVM format as requiered for the model.
        if (verbose) {
            String msg = String.format("Transforming model data into LibSVM files.");
            System.out.println(msg);
        }

        String libsvmPathTrain = targetPath.toString() + "/Dry_Bean_Dataset_train.libsvm";
        String libsvmPathTest = targetPath.toString() + "/Dry_Bean_Dataset_test.libsvm";

        TransformToLibsvm transformToLibsvm = new TransformToLibsvm();
        transformToLibsvm.transform((ArrayList<RawDatasource>) datasets.get("train"), libsvmPathTrain);
        transformToLibsvm.transform((ArrayList<RawDatasource>) datasets.get("test"), libsvmPathTest);

        // Define and train the model.
        if (verbose) {
            System.out.println("Define and train the model");
        }

        String modelConfigPath = targetPath.getParent().toString() + "/basicXGBoostModelConfig.json";
        String encoderPath = targetPath.getParent().toString() + "/encoded.json";

        String modelPath = targetPath.getParent().toString() + "/model.bin";
        BasicXGBoostModel basicXGBoostModel  = new BasicXGBoostModel();
        Booster model = basicXGBoostModel.train(libsvmPathTrain, libsvmPathTest, modelPath, modelConfigPath, 50);

        if (verbose) {
            System.out.println("Verifying model.");
        }

        new ModelEvaluation(model, libsvmPathTrain, libsvmPathTest, datasets, modelConfigPath, encoderPath).report();


    }



}