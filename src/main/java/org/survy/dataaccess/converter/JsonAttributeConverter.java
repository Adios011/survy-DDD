package org.survy.dataaccess.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.survy.domain.core.valueObject.Option;

import javax.print.attribute.Attribute;
import java.util.List;

@Converter
public class JsonAttributeConverter implements AttributeConverter<List<Option>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    {

    }
    @Override
    public String convertToDatabaseColumn(List<Option> attribute) {


        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Option> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class , Option.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
