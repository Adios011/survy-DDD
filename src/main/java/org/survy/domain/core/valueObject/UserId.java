package org.survy.domain.core.valueObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties("valid")
public class UserId extends BaseId<UUID>{
    public UserId(UUID value) {
        super(value);
    }

    public UserId() {
        super(UUID.randomUUID());
    }

    public boolean isValid(){
        return getValue() != null;
    }
}
