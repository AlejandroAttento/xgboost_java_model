package mlModels;

import ml.dmlc.xgboost4j.java.XGBoostError;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import ml.dmlc.xgboost4j.java.Booster;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import utils.JsonReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BasicXGBoostModel {
    public static @NotNull Booster train(String trainPath, String testPath, String modelPath, String configPath, Integer rounds) throws XGBoostError, IOException, ParseException {

        DMatrix trainMat = new DMatrix(trainPath);
        DMatrix testMat = new DMatrix(testPath);

        Map<String, DMatrix> watches = new HashMap<String, DMatrix>() {
            {
                put("train", trainMat);
                put("test", testMat);
            }
        };

        JSONObject jsonObject = new JsonReader().read(configPath);

        // Define the booster parameters
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("eta", Double.parseDouble(jsonObject.get("eta").toString()));
        params.put("max_depth", Integer.parseInt(jsonObject.get("max_depth").toString()));
        params.put("objective", jsonObject.get("objective").toString());
        params.put("eval_metric", jsonObject.get("eval_metric").toString());
        params.put("num_class", Integer.parseInt(jsonObject.get("num_class").toString()));

        // Train the model
        Booster booster = XGBoost.train(trainMat, params, rounds, watches, null, null);

        // Save the model to a file
        booster.saveModel(modelPath);

        // Make predictions
        return booster;

    }
}
