package dataModels;

public class RawDatasource {
    public String beanClass;
    public Double area;
    public Double perimeter;
    public Double majorAxisLength;
    public Double minorAxisLength;
    public Double aspectRatio;
    public Double eccentricity;
    public Double convexArea;
    public Double equivDiameter;
    public Double extent;
    public Double solidity;
    public Double roundness;
    public Double compactness;
    public Double shapeFactor1;
    public Double shapeFactor2;
    public Double shapeFactor3;
    public Double shapeFactor4;

    public RawDatasource(String beanClass, String area, String perimeter, String majorAxisLength,
                         String minorAxisLength, String aspectRatio, String eccentricity, String convexArea,
                         String equivDiameter, String extent, String solidity, String roundness, String compactness,
                         String shapeFactor1, String shapeFactor2, String shapeFactor3, String shapeFactor4) {
        this.beanClass = beanClass;
        this.area = Double.parseDouble(area);
        this.perimeter = Double.parseDouble(perimeter);
        this.majorAxisLength = Double.parseDouble(majorAxisLength);
        this.minorAxisLength = Double.parseDouble(minorAxisLength);
        this.aspectRatio = Double.parseDouble(aspectRatio);
        this.eccentricity = Double.parseDouble(eccentricity);
        this.convexArea = Double.parseDouble(convexArea);
        this.equivDiameter = Double.parseDouble(equivDiameter);
        this.extent = Double.parseDouble(extent);
        this.solidity = Double.parseDouble(solidity);
        this.roundness = Double.parseDouble(roundness);
        this.compactness = Double.parseDouble(compactness);
        this.shapeFactor1 = Double.parseDouble(shapeFactor1);
        this.shapeFactor2 = Double.parseDouble(shapeFactor2);
        this.shapeFactor3 = Double.parseDouble(shapeFactor3);
        this.shapeFactor4 = Double.parseDouble(shapeFactor4);

    }
}
