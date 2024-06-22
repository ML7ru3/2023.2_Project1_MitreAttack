package hust.cybersec.conversion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Converts YAML files to JSON format.
 */
public class YamlToJsonConverter {
    private final String yamlFilePath;
    private final String jsonFilePath;
    private final ObjectMapper yamlMapper;
    private final ObjectMapper jsonMapper;


    public YamlToJsonConverter(String yamlFilePath, String jsonFilePath) {
        this.yamlFilePath = yamlFilePath;
        this.jsonFilePath = jsonFilePath;

        // Setup YAML factory with custom options to handle larger files
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(10 * 1024 * 1024);  // Allow up to 10MB
        YAMLFactory yamlFactory = YAMLFactory.builder().loaderOptions(loaderOptions).build();

        // Create object mappers for YAML and JSON
        this.yamlMapper = new ObjectMapper(yamlFactory);
        this.jsonMapper = new ObjectMapper();
    }



    private byte[] readYamlFile() throws IOException {
        Path path = Path.of(yamlFilePath).normalize();
        if (!Files.isRegularFile(path)) {
            throw new FileNotFoundException("Could not find the YAML file at " + yamlFilePath);
        }
        return Files.readAllBytes(path);
    }


    private Object convertYamlToJson(byte[] yamlBytes) {
        try {
            return yamlMapper.readValue(yamlBytes, Object.class);
        } catch (IOException e) {
            System.err.println("Error converting YAML to JSON: " + e.getMessage());
            return null;
        }
    }


    private void writeJsonToFile(Object json) throws IOException {
        try (OutputStream out = new FileOutputStream(jsonFilePath);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(out)) {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(bufferedOut, json);
        }
    }
    public void convert() {
        System.out.println("Converting index.yaml to index.json");

        try {
            byte[] yamlBytes = readYamlFile();
            Object json = convertYamlToJson(yamlBytes);
            writeJsonToFile(json);
        } catch (IOException e) {
            System.err.println("An error occurred during YAML to JSON conversion.");
        }

        System.out.println("Convert completed!");
    }

}


