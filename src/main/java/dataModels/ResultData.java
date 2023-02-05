package dataModels;

public class ResultData {
    public String beanClass;
    public Integer truePredictions;
    public Integer falsePredictions;

    public ResultData(String beanClass, Integer truePredictions, Integer falsePredictions) {
        this.beanClass = beanClass;
        this.truePredictions = truePredictions;
        this.falsePredictions = falsePredictions;
    }
}