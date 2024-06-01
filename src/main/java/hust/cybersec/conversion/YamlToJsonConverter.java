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

    /**
     * Initializes the converter with file paths for input and output.
     *
     * @param yamlFilePath Path to the input YAML file
     * @param jsonFilePath Path to the output JSON file
     */
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


    /**
     * Reads the entire YAML file into a byte array.
     *
     * @return Byte array containing the YAML file's data.
     * @throws IOException if there are issues reading the file.
     */
    private byte[] readYamlFile() throws IOException {
        Path path = Path.of(yamlFilePath).normalize();
        if (!Files.isRegularFile(path)) {
            throw new FileNotFoundException("Could not find the YAML file at " + yamlFilePath);
        }
        return Files.readAllBytes(path);
    }

    /**
     * Converts the YAML data to a JSON object.
     *
     * @param yamlBytes Raw YAML data.
     * @return Converted JSON object.
     */
    private Object convertYamlToJson(byte[] yamlBytes) {
        try {
            return yamlMapper.readValue(yamlBytes, Object.class);
        } catch (IOException e) {
            System.err.println("Error converting YAML to JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Writes the JSON object to a file.
     *
     * @param json Object to write.
     * @throws IOException if there are issues writing the file.
     */
    private void writeJsonToFile(Object json) throws IOException {
        try (OutputStream out = new FileOutputStream(jsonFilePath);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(out)) {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(bufferedOut, json);
        }
    }
}
