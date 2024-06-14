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
    public MitreAttackFramework() {}

    /**
     * Constructor for the MitreAttackFramework class.
     *
     * @param techniqueId             The ID of the technique.
     * @param techniqueName           The name of the technique.
     * @param techniqueDescription    The description of the technique.
     * @param techniquePlatforms      The platforms associated with the technique.
     * @param techniqueDomains        The domains associated with the technique.
     * @param techniqueUrl            The URL of the technique.
     * @param techniqueTactics        The tactics associated with the technique.
     * @param techniqueDetection      The detection information for the technique.
     * @param techniqueIsSubtechnique Indicates if the technique is a subtechnique.
     */
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

    // Getters and Setters
    public String getTechniqueId() {
        return techniqueId;
    }

    public void setTechniqueId(String techniqueId) {
        this.techniqueId = techniqueId;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    public String getTechniqueDescription() {
        return techniqueDescription;
    }

    public void setTechniqueDescription(String techniqueDescription) {
        this.techniqueDescription = techniqueDescription;
    }

    public String[] getTechniquePlatforms() {
        return techniquePlatforms;
    }

    public void setTechniquePlatforms(String[] techniquePlatforms) {
        this.techniquePlatforms = techniquePlatforms;
    }

    public String[] getTechniqueDomains() {
        return techniqueDomains;
    }

    public void setTechniqueDomains(String[] techniqueDomains) {
        this.techniqueDomains = techniqueDomains;
    }

    public String getTechniqueUrl() {
        return techniqueUrl;
    }

    public void setTechniqueUrl(String techniqueUrl) {
        this.techniqueUrl = techniqueUrl;
    }

    public String[] getTechniqueTactics() {
        return techniqueTactics;
    }

    public void setTechniqueTactics(String[] techniqueTactics) {
        this.techniqueTactics = techniqueTactics;
    }

    public String getTechniqueDetection() {
        return techniqueDetection;
    }

    public void setTechniqueDetection(String techniqueDetection) {
        this.techniqueDetection = techniqueDetection;
    }

    public boolean isTechniqueIsSubtechnique() {
        return techniqueIsSubtechnique;
    }

    public void setTechniqueIsSubtechnique(boolean techniqueIsSubtechnique) {
        this.techniqueIsSubtechnique = techniqueIsSubtechnique;
    }

    /**
     * Downloads Mitre Attack data.
     *
     * @throws URISyntaxException If there is an error in the URI syntax.
     */
//    public void downloadData() throws URISyntaxException {
//        final String MITRE_URL = "https://raw.githubusercontent.com/mitre-attack/attack-stix-data/master/";
//        final String MITRE_DIRECTORY = "./data/mitre-attack";
//
//        // Array of files to download
//        final String[] MITRE_FILES = {"enterprise-attack/enterprise-attack.json", "mobile-attack/mobile-attack.json",
//                "ics-attack/ics-attack.json"};
//
//        dataGetter mitreDownloader = new dataGetter(MITRE_URL, MITRE_DIRECTORY);
//        mitreDownloader.downloadData();
    }

