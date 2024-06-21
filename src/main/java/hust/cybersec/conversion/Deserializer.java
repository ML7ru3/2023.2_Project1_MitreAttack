package hust.cybersec.conversion;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hust.cybersec.exportexcel.JsonNodeHandler;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Deserializer extends JsonDeserializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonNodeHandler jsonHandler = new JsonNodeHandler();

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return node.has("auto_generated_guid") ? deserializeAtomicRedTeam(node) : deserializeMitreAttackFramework(node);
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

    private String[] parseStringArray(ObjectMapper objectMapper, JsonNode node, String fieldName)
            throws JsonProcessingException {
        return node.has(fieldName) ? objectMapper.treeToValue(node.get(fieldName), String[].class) : new String[0];
    }

    private String[] parseTactics(JsonNode node) {
        if (!node.has("kill_chain_phases")) return new String[0];

        JsonNode phasesNode = node.get("kill_chain_phases");
        List<String> phaseNames = new ArrayList<>();
        if (phasesNode != null && phasesNode.isArray()) {
            for (JsonNode phaseNode : phasesNode) {
                if (phaseNode.has("phase_name")) {
                    phaseNames.add(phaseNode.get("phase_name").asText());
                }
            }
        }
        return phaseNames.toArray(new String[0]);
    }

    private String[] parseInputArguments(JsonNode node) {
        if (!node.has("input_arguments")) return new String[0];

        JsonNode argsNode = node.get("input_arguments");
        List<String> inputArgsList = new ArrayList<>();
        if (argsNode != null && argsNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = argsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                StringBuilder combinedValues = new StringBuilder("* ").append(field.getKey()).append(":\n        ");
                JsonNode subNode = field.getValue();
                subNode.fields().forEachRemaining(subField -> {
                    combinedValues.append(subField.getKey()).append(": ").append(subField.getValue()).append("\n        ");
                });
                inputArgsList.add(combinedValues.toString());
            }
        }
        return inputArgsList.toArray(new String[0]);
    }

    private String[] parseExecutor(JsonNode node) {
        if (!node.has("executor")) return new String[0];

        JsonNode executorNode = node.get("executor");
        List<String> executorList = new ArrayList<>();
        if (executorNode != null && executorNode.isObject()) {
            executorNode.fields().forEachRemaining(field -> {
                StringBuilder combinedValues = new StringBuilder("* ").append(field.getKey()).append(": ");
                combinedValues.append(field.getValue());
                executorList.add(combinedValues.toString());
            });
        }
        return executorList.toArray(new String[0]);
    }

    private String[] parseDependencies(JsonNode node) {
        if (!node.has("dependencies")) return new String[0];

        JsonNode dependenciesNode = node.get("dependencies");
        List<String> dependenciesList = new ArrayList<>();
        if (dependenciesNode != null && dependenciesNode.isArray()) {
            for (JsonNode dependencyNode : dependenciesNode) {
                dependencyNode.fields().forEachRemaining(field -> {
                    StringBuilder combinedValues = new StringBuilder("* ").append(field.getKey()).append(": ")
                            .append(field.getValue());
                    dependenciesList.add(combinedValues.toString());
                });
                dependenciesList.add("\n");
            }
        }
        return dependenciesList.toArray(new String[0]);
    }

    private MitreAttackFramework deserializeMitreAttackFramework(JsonNode node) throws JsonProcessingException {
        JsonNode externalReferencesNode = node.get("external_references");
        JsonNode externalNode = findExternalNode(externalReferencesNode);
        String techniqueId = externalNode != null && externalNode.has("external_id") ? externalNode.get("external_id").asText() : "";
        String techniqueName = jsonHandler.getNodeValue(node, "name");
        String techniqueDescription = jsonHandler.getNodeValue(node, "description");
        String[] techniquePlatforms = parseStringArray(objectMapper, node, "x_mitre_platforms");
        String[] techniqueDomains = parseStringArray(objectMapper, node, "x_mitre_domains");
        String techniqueUrl = externalNode != null && externalNode.has("url") ? externalNode.get("url").asText() : "";
        String[] techniqueTactics = parseTactics(node);
        String techniqueDetection = jsonHandler.getNodeValue(node, "x_mitre_detection");
        boolean techniqueIsSubtechnique = node.has("x_mitre_is_subtechnique") && node.get("x_mitre_is_subtechnique").asBoolean();

        return new MitreAttackFramework(techniqueId, techniqueName, techniqueDescription, techniquePlatforms,
                techniqueDomains, techniqueUrl, techniqueTactics, techniqueDetection, techniqueIsSubtechnique);
    }

    private AtomicRedTeam deserializeAtomicRedTeam(JsonNode node) throws JsonProcessingException {
        String testName = jsonHandler.getNodeValue(node, "name");
        String testGuid = jsonHandler.getNodeValue(node, "auto_generated_guid");
        String testDescription = jsonHandler.getNodeValue(node, "description");
        String[] testSupportedPlatforms = parseStringArray(objectMapper, node, "supported_platforms");
        String[] testInputArguments = parseInputArguments(node);
        String[] testExecutor = parseExecutor(node);
        String testDependencyExecutorName = jsonHandler.getNodeValue(node, "dependency_executor_name");
        String[] testDependencies = parseDependencies(node);

        return new AtomicRedTeam(testName, testGuid, testDescription, testSupportedPlatforms, testInputArguments,
                testExecutor, testDependencyExecutorName, testDependencies);
    }
}
