package hust.cybersec.conversion;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeHandler {

    public String getNodeValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asText() : "";
    }

    public boolean checkValid(JsonNode root) {
        if (!getNodeValue(root, "type").equals("attack-pattern") ||
                getNodeValue(root, "revoked").equals("true") ||
                getNodeValue(root, "x_mitre_deprecated").equals("true") ||
                !root.has("external_references")) {
            return false;
        }

        JsonNode externalReferencesNode = root.get("external_references");
        if (externalReferencesNode != null && externalReferencesNode.isArray()) {
            for (JsonNode referenceNode : externalReferencesNode) {
                if (referenceNode.has("external_id") &&
                        referenceNode.get("source_name").asText().equals("mitre-attack") &&
                        referenceNode.get("external_id").asText().startsWith("T")) {
                    return true;
                }
            }
        }
        return false;
    }
}
