package hust.cybersec.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static hust.cybersec.data.Constant.*;

public class DataProcessing {


    private List<AtomicRedTeam> listAtomics;
    private List<MitreAttackFramework> listMobiles, listICSs, listEnterprises;
    final JsonNodeHandler jsonHandler = new JsonNodeHandler();

    public DataProcessing() throws IOException {
        //Mapping all json files to JsonNode
        this.listAtomics = mapAtomicTests();
        this.listMobiles = mapMitreAttackFramework(MOBILE_FILE_PATH);
        this.listICSs = mapMitreAttackFramework(ICS_FILE_PATH);
        this.listEnterprises = mapMitreAttackFramework(ENTERPRISE_FILE_PATH);
    }


    private List<MitreAttackFramework> mapMitreAttackFramework (String path) throws IOException{
        String jsonData = new String(Files.readAllBytes(Paths.get(path)));
        HashSet<String> testID = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);
        List<MitreAttackFramework> mitreAttack = new ArrayList<>();

        ArrayNode techniqueList = objectMapper.valueToTree(root.get("objects"));

        for (JsonNode techniqueNode : techniqueList) {
            if (!jsonHandler.checkValid(techniqueNode)) {
                continue;
            }
            MitreAttackFramework technique = objectMapper.treeToValue(techniqueNode, MitreAttackFramework.class);
            mitreAttack.add(technique);
        }

        return mitreAttack;
    }

    private List<AtomicRedTeam> mapAtomicTests() throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get(ATOMIC_FILE_PATH)));
        HashSet<String> testID = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonData);
        List<AtomicRedTeam> atomicTests = new ArrayList<>();

        for (JsonNode tacticList : root) {
            for (JsonNode list : tacticList) {
                JsonNode techniqueNode = list.get("technique");
                JsonNode atomicTestsNode = list.get("atomic_tests");

                if (!jsonHandler.checkValid(techniqueNode)) {
                    continue;
                }

                if (atomicTestsNode != null && atomicTestsNode.isArray()) {
                    MitreAttackFramework technique = objectMapper.treeToValue(techniqueNode, MitreAttackFramework.class);
                    int testNumber = 0;

                    for (JsonNode atomicTestNode : atomicTestsNode) {
                        if (!atomicTestNode.has("auto_generated_guid")) {
                            continue;
                        }

                        String testAutoID = atomicTestNode.get("auto_generated_guid").asText();

                        if (testID.contains(testAutoID)) {
                            continue;
                        }

                        testID.add(testAutoID);
                        testNumber++;

                        AtomicRedTeam atomicTest = objectMapper.treeToValue(atomicTestNode, AtomicRedTeam.class);
                        atomicTest.setTechniqueDetailsFromFramework(technique);
                        atomicTest.setTestNumber(testNumber);

                        atomicTests.add(atomicTest);
                    }
                }
            }
        }

        return atomicTests;
    }


    public List<AtomicRedTeam> getListAtomics() {
        return listAtomics;
    }

    public List<MitreAttackFramework> getListMobiles() {
        return listMobiles;
    }

    public List<MitreAttackFramework> getListICSs() {
        return listICSs;
    }

    public List<MitreAttackFramework> getListEnterprises() {
        return listEnterprises;
    }
}
