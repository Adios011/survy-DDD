package org.survy.domain.core.valueObject;

import java.io.Serializable;
import java.util.Objects;

public class Option implements Serializable {

    private  String label;
    private int order;


    public Option() {
    }

    public Option(String label) {
        this.label = label;
    }

    public Option(String label , int order) {
        this.label = label;
        this.order = order;
    }

    public  boolean valid(){
        return label != null && !label.isEmpty();
    }

    public String getLabel() {
        return label;
    }

    public int getOrder() {
        return order;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return order == option.order && Objects.equals(label, option.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, order);
    }


    public void setOrder(int order) {
        this.order = order;
    }
}
