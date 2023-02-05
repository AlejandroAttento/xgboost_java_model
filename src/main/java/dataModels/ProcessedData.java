package dataModels;

public class ProcessedData {
    public String beanClass;
    public String predictedBeanClass;
    public Boolean predictionIndicator;
    public String context;

    public ProcessedData(String beanClass, String predictedBeanClass, Boolean predictionIndicator, String context) {
        this.beanClass = beanClass;
        this.predictedBeanClass = predictedBeanClass;
        this.predictionIndicator = predictionIndicator;
        this.context = context;
    }
}