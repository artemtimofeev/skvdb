package org.skvdb.storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private FileWriter writer;

    public void initWriter(){
        try {
            writer = new FileWriter("storage.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dumpToStorage(Map<String, String> container) {
        for (Map.Entry<String, String> keyValue : container.entrySet()) {
            addToDisk(keyValue.getKey(), keyValue.getValue());
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToDisk(String key, String value) {
        try {
            writer.write(key + ":" + value + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public Map<String, String> readFromDisk() {
        String fileName = "storage.txt";
        String line;

        Map<String, String> container = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                String[] keyValue = line.split(":");
                String key = keyValue[0];
                String value = keyValue[1];
                container.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return container;
    }
}
