package org.survy.domain.core.valueObject;

public class WeightedOption  extends Option{

    private  int weight;

     WeightedOption(){

    }


    public WeightedOption(String label , int weight) {
        this(label, weight , 0);
    }

    public WeightedOption(String label, int weight, int order) {
        super(label, order);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
