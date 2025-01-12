package org.survy.domain.core.valueObject;

import java.util.Objects;

public class BaseId<T> {
    private  T value ;

    public BaseId(T value) {
        this.value = value;
    }

    public BaseId() {
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return Objects.equals(value, baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
