package org.skvdb.storage.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Iterator;
import java.util.Map;

public class StructedTable<Value> {

    private final BaseTable table;

    private final Class<?> valueClass;

    public StructedTable(BaseTable table, Class<?> valueClass) {
        this.valueClass = valueClass;
        this.table = table;
    }

    public Value get(String key) {
        try {
            return parseFromJson(table.get(key));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String key, Value value) {
        try {
            table.set(key, convertToJson(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {
        table.delete(key);
    }

    public boolean containsKey(String key) {
        return table.containsKey(key);
    }

    public TableMetaData getTableMetaData() {
        return table.getTableMetaData();
    }

    public Iterator<Map.Entry<String, Value>> getIterator() {
        var iterator = table.getIterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<String, Value> next() {
                Map.Entry<String, String> entry = iterator.next();
                return new Map.Entry<String, Value>() {
                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public Value getValue() {
                        try {
                            return parseFromJson(entry.getValue());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public Value setValue(Value value) {
                        try {
                            return parseFromJson(entry.setValue(convertToJson(value)));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };
    }

    public Value parseFromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return (Value) objectMapper.readValue(json, valueClass);
    }

    public String convertToJson(Value value) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(value);
    }
}
