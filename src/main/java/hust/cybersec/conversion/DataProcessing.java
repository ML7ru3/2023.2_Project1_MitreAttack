package hust.cybersec.conversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static hust.cybersec.data.Constant.*;

public class DataProcessing {


    private final List<AtomicRedTeam> listAtomics;
    private final List<MitreAttackFramework> listMobiles, listICSs, listEnterprises;
    final JsonNodeHandler jsonHandler = new JsonNodeHandler();


    //This contains all attack that atomics has tested.
    private final HashSet<String> allCoveredTest = new HashSet<>();

    private final Map<String, Integer> platforms = new HashMap<>();
    private final Map<String, Integer> coveredPlatforms = new HashMap<>();

    private final Map<String, Integer> tactics = new HashMap<>();
    private final Map<String, Integer> coveredTactics = new HashMap<>();

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
                ArrayNode atomicTestsNode = (ArrayNode) list.get("atomic_tests");
                //check valid
                if (!jsonHandler.checkValid(techniqueNode)) {
                    continue;
                }

                //get all platform that in atomic test (enterprise)
                getEssentialData(techniqueNode, platforms, tactics);

                if (atomicTestsNode != null && atomicTestsNode.isArray()) {
                    //check if this technique is tested by atomic red team
                    if (!atomicTestsNode.isEmpty()) {
                        //This is covered test
                        allCoveredTest.add(techniqueNode.get("id").asText());
                        getEssentialData(techniqueNode, coveredPlatforms, coveredTactics);
                    }
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

    private void getEssentialData(JsonNode techniqueNode, Map<String, Integer> coveredPlatforms, Map<String, Integer> coveredTactics) {
        for (JsonNode node : techniqueNode.get("x_mitre_platforms")){
            coveredPlatforms.put(node.asText(), coveredPlatforms.getOrDefault(node.asText(), 0) + 1);
        }

        ArrayNode killChainPhases = (ArrayNode) techniqueNode.get("kill_chain_phases");
        if (killChainPhases.isEmpty()) return;
        for (JsonNode tactic : killChainPhases){
            coveredTactics.put(tactic.get("phase_name").asText(), coveredTactics.getOrDefault(tactic.get("phase_name").asText(), 0) + 1);
        }


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

    public Map<String, Integer> getPlatforms() {
        return platforms;
    }

    public Map<String, Integer> getCoveredPlatforms() {
        return coveredPlatforms;
    }

    public HashSet<String> getAllCoveredTest() {
        return allCoveredTest;
    }

    public Map<String, Integer> getTactics() {
        return tactics;
    }

    public Map<String, Integer> getCoveredTactics() {
        return coveredTactics;
    }
}
