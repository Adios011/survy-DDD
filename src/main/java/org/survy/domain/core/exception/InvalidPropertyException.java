package org.survy.domain.core.exception;


import org.springframework.lang.Nullable;

public class InvalidPropertyException  extends RuntimeException{

    private final Class<?> entityClass;
    private final String propertyName;

    public InvalidPropertyException(Class<?> entityClass, String propertyName, String message) {
        super("Invalid property '" + propertyName + "' of bean class [" + entityClass.getName() + "]: " + message);
        this.entityClass = entityClass;
        this.propertyName = propertyName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
