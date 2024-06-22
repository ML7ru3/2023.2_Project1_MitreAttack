package hust.cybersec.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hust.cybersec.collector.dataGetter;
import hust.cybersec.conversion.Deserializer;

import java.net.URISyntaxException;

/**
 * Represents the Mitre Attack Framework class.
 */
@JsonDeserialize(using = Deserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MitreAttackFramework {
    private String techniqueId;

    @JsonProperty("name")
    private String techniqueName;

    @JsonProperty("description")
    private String techniqueDescription;

    @JsonProperty("x_mitre_platforms")
    private String[] techniquePlatforms;

    @JsonProperty("x_mitre_domains")
    private String[] techniqueDomains;

    private String techniqueUrl;

    private String[] techniqueTactics;

    @JsonProperty("x_mitre_detection")
    private String techniqueDetection;

    @JsonProperty("x_mitre_is_subtechnique")
    private boolean techniqueIsSubtechnique;

    /**
     * Default constructor for the MitreAttackFramework class.
     */
    public MitreAttackFramework() {

    }


    public MitreAttackFramework(String techniqueId, String techniqueName, String techniqueDescription,
                                String[] techniquePlatforms, String[] techniqueDomains, String techniqueUrl, String[] techniqueTactics,
                                String techniqueDetection, boolean techniqueIsSubtechnique) {
        this.techniqueId = techniqueId;
        this.techniqueName = techniqueName;
        this.techniqueDescription = techniqueDescription;
        this.techniquePlatforms = techniquePlatforms;
        this.techniqueDomains = techniqueDomains;
        this.techniqueUrl = techniqueUrl;
        this.techniqueTactics = techniqueTactics;
        this.techniqueDetection = techniqueDetection;
        this.techniqueIsSubtechnique = techniqueIsSubtechnique;
    }

    // Getter and Setter
    public String getTechniqueId() {
        return techniqueId;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueId(String techniqueId) {
        this.techniqueId = techniqueId;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    public void setTechniqueDescription(String techniqueDescription) {
        this.techniqueDescription = techniqueDescription;
    }

    public void setTechniquePlatforms(String[] techniquePlatforms) {
        this.techniquePlatforms = techniquePlatforms;
    }

    public void setTechniqueDomains(String[] techniqueDomains) {
        this.techniqueDomains = techniqueDomains;
    }

    public void setTechniqueUrl(String techniqueUrl) {
        this.techniqueUrl = techniqueUrl;
    }

    public void setTechniqueTactics(String[] techniqueTactics) {
        this.techniqueTactics = techniqueTactics;
    }

    public void setTechniqueDetection(String techniqueDetection) {
        this.techniqueDetection = techniqueDetection;
    }

    public void setTechniqueIsSubtechnique(boolean techniqueIsSubtechnique) {
        this.techniqueIsSubtechnique = techniqueIsSubtechnique;
    }

    public String getTechniqueDescription() {
        return techniqueDescription;
    }

    public String[] getTechniquePlatforms() {
        return techniquePlatforms;
    }

    public String[] getTechniqueDomains() {
        return techniqueDomains;
    }

    public String getTechniqueUrl() {
        return techniqueUrl;
    }

    public String[] getTechniqueTactics() {
        return techniqueTactics;
    }

    public String getTechniqueDetection() {
        return techniqueDetection;
    }

    public void download() throws URISyntaxException {
        final String[] MITRE_URL = {"https://api.github.com/repos/mitre-attack/attack-stix-data/contents/enterprise-attack/enterprise-attack.json",
                "https://api.github.com/repos/mitre-attack/attack-stix-data/contents/ics-attack/ics-attack.json",
                "https://api.github.com/repos/mitre-attack/attack-stix-data/contents/mobile-attack/mobile-attack.json"};
        final String[] NAME_FILE = {"enterprise-attack.json",
                "ics-attack.json",
                "mobile-attack.json"};

        for (int i = 0; i < 3; i++){
            dataGetter mitreRetriever = new dataGetter(MITRE_URL[i], NAME_FILE[i]);
            try {
                mitreRetriever.retrieveData(false);
            } catch(Exception e){
                System.err.println("Cannot download data due to some errors!");
            }
        }
    }



        public boolean isTechniqueIsSubtechnique() {
        return techniqueIsSubtechnique;
    }
    public void setTechniqueDetailsFromFramework(MitreAttackFramework technique) {
        this.setTechniqueId(technique.getTechniqueId());
        this.setTechniqueName(technique.getTechniqueName());
        this.setTechniqueDescription(technique.getTechniqueDescription());
        this.setTechniquePlatforms(technique.getTechniquePlatforms());
        this.setTechniqueDomains(technique.getTechniqueDomains());
        this.setTechniqueUrl(technique.getTechniqueUrl());
        this.setTechniqueTactics(technique.getTechniqueTactics());
        this.setTechniqueDetection(technique.getTechniqueDetection());
        this.setTechniqueIsSubtechnique(technique.isTechniqueIsSubtechnique());
    }
}


