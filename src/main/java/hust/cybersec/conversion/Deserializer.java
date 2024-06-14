package hust.cybersec.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import hust.cybersec.validation.JsonNodeHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Deserializer class is responsible for deserializing JSON data into objects of type AtomicRedTeam or MitreAttackFramework.
 */
public class Deserializer extends JsonDeserializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonNodeHandler jsonHandler = new JsonNodeHandler();

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("auto_generated_guid")) {
            return deserializeAtomicRedTeam(node);
        } else {
            return deserializeMitreAttackFramework(node);
        }
    }

    private JsonNode findExternalNode(JsonNode externalReferencesNode) {
        if (externalReferencesNode != null && externalReferencesNode.isArray()) {
            for (JsonNode referenceNode : externalReferencesNode) {
                if (referenceNode.has("external_id") && "mitre-attack".equals(referenceNode.get("source_name").asText())) {
                    return referenceNode;
                }
            }
        }
        return null;
    }

    private String[] parseStringArray(JsonNode node, String fieldName) throws JsonProcessingException {
        if (node.has(fieldName)) {
            return objectMapper.treeToValue(node.get(fieldName), String[].class);
        }
        return new String[0];
    }

    private String[] parseTactics(JsonNode node) {
        if (!node.has("kill_chain_phases")) {
            return new String[0];
        }
        List<String> phaseNames = new ArrayList<>();
        for (JsonNode phaseNode : node.get("kill_chain_phases")) {
            if (phaseNode.has("phase_name")) {
                phaseNames.add(phaseNode.get("phase_name").asText());
            }
        }
        return phaseNames.toArray(new String[0]);
    }

    private String[] parseInputArguments(JsonNode node) {
        if (!node.has("input_arguments")) {
            return new String[0];
        }
        List<String> inputArgumentsList = new ArrayList<>();
        JsonNode jsonNode = node.get("input_arguments");
        if (jsonNode.isObject()) {
            for (Iterator<Map.Entry<String, JsonNode>> argumentFields = jsonNode.fields(); argumentFields.hasNext(); ) {
                Map.Entry<String, JsonNode> argumentField = argumentFields.next();
                StringBuilder combinedValues = new StringBuilder();
                combinedValues.append("* ").append(argumentField.getKey()).append(":\n        ");
                for (Iterator<Map.Entry<String, JsonNode>> subNodeFields = argumentField.getValue().fields(); subNodeFields.hasNext(); ) {
                    Map.Entry<String, JsonNode> subNodeField = subNodeFields.next();
                    combinedValues.append(subNodeField.getKey()).append(": ").append(subNodeField.getValue()).append("\n        ");
                }
                inputArgumentsList.add(combinedValues.toString());
            }
        }
        return inputArgumentsList.toArray(new String[0]);
    }

    private String[] parseExecutor(JsonNode node) {
        if (!node.has("executor")) {
            return new String[0];
        }
        List<String> executorList = new ArrayList<>();
        JsonNode jsonNode = node.get("executor");
        if (jsonNode.isObject()) {
            for (Iterator<Map.Entry<String, JsonNode>> executorFields = jsonNode.fields(); executorFields.hasNext(); ) {
                Map.Entry<String, JsonNode> executorField = executorFields.next();
                StringBuilder combinedValues = new StringBuilder();
                combinedValues.append("* ").append(executorField.getKey()).append(": ").append(executorField.getValue());
                executorList.add(combinedValues.toString());
            }
        }
        return executorList.toArray(new String[0]);
    }

    private String[] parseDependencies(JsonNode node) {
        if (!node.has("dependencies")) {
            return new String[0];
        }
        List<String> dependenciesList = new ArrayList<>();
        JsonNode jsonNode = node.get("dependencies");
        if (jsonNode.isArray()) {
            for (JsonNode dependencyNode : jsonNode) {
                for (Iterator<Map.Entry<String, JsonNode>> dependencyFields = dependencyNode.fields(); dependencyFields.hasNext(); ) {
                    Map.Entry<String, JsonNode> dependencyField = dependencyFields.next();
                    StringBuilder combinedValues = new StringBuilder();
                    combinedValues.append("* ").append(dependencyField.getKey()).append(": ").append(dependencyField.getValue());
                    dependenciesList.add(combinedValues.toString());
                }
                dependenciesList.add("\n");
            }
        }
        return dependenciesList.toArray(new String[0]);
    }

    private MitreAttackFramework deserializeMitreAttackFramework(JsonNode node)
            throws JsonProcessingException, IllegalArgumentException {

        JsonNode externalNode = findExternalNode(node.get("external_references"));
        String techniqueId = externalNode != null && externalNode.has("external_id") ? externalNode.get("external_id").asText() : "";

        String techniqueName = jsonHandler.getNodeValue(node, "name");
        String techniqueDescription = jsonHandler.getNodeValue(node, "description");
        String[] techniquePlatforms = parseStringArray(node, "x_mitre_platforms");
        String[] techniqueDomains = parseStringArray(node, "x_mitre_domains");
        String techniqueUrl = externalNode != null && externalNode.has("url") ? externalNode.get("url").asText() : "";
        String[] techniqueTactics = parseTactics(node);
        String techniqueDetection = jsonHandler.getNodeValue(node, "x_mitre_detection");
        boolean techniqueIsSubtechnique = node.has("x_mitre_is_subtechnique") && node.get("x_mitre_is_subtechnique").asBoolean();

        return new MitreAttackFramework(techniqueId, techniqueName, techniqueDescription, techniquePlatforms,
                techniqueDomains, techniqueUrl, techniqueTactics, techniqueDetection, techniqueIsSubtechnique);
    }

    private AtomicRedTeam deserializeAtomicRedTeam(JsonNode node)
            throws JsonProcessingException, IllegalArgumentException {
        String testName = jsonHandler.getNodeValue(node, "name");
        String testGuid = jsonHandler.getNodeValue(node, "auto_generated_guid");
        String testDescription = jsonHandler.getNodeValue(node, "description");
        String[] testSupportedPlatforms = parseStringArray(node, "supported_platforms");
        String[] testInputArguments = parseInputArguments(node);
        String[] testExecutor = parseExecutor(node);
        String testDependencyExecutorName = jsonHandler.getNodeValue(node, "dependency_executor_name");
        String[] testDependencies = parseDependencies(node);

        return new AtomicRedTeam(testName, testGuid, testDescription, testSupportedPlatforms, testInputArguments,
                testExecutor, testDependencyExecutorName, testDependencies);
    }
}
