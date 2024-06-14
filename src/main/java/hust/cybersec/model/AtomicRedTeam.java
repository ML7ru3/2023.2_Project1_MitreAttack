package hust.cybersec.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import hust.cybersec.collector.dataGetter;
import hust.cybersec.conversion.YamlToJsonConverter;
import hust.cybersec.excelexporter.JsonToExcelConverter;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Represents the Atomic Red Team class, extending the MitreAttackFramework.
 * Provides functionality related to Atomic Red Team tests.
 */
public class AtomicRedTeam extends MitreAttackFramework {
    private int testNumber;

    @JsonProperty("name")
    private String testName;

    @JsonProperty("auto_generated_guid")
    private String testGuid;

    @JsonProperty("description")
    private String testDescription;

    @JsonProperty("supported_platforms")
    private String[] testSupportedPlatforms;

    @JsonProperty("input_arguments")
    private String[] testInputArguments;

    @JsonProperty("executor")
    private String[] testExecutor;

    @JsonProperty("dependency_executor_name")
    private String testDependencyExecutorName;

    @JsonProperty("dependencies")
    private String[] testDependencies;

    /**
     * Default constructor for the AtomicRedTeam class.
     * Initializes the class with default values.
     */
    public AtomicRedTeam() {
        super();
    }

    /**
     * Constructor for the AtomicRedTeam class.
     *
     * @param testName                   The name of the test.
     * @param testGuid                   The auto-generated GUID for the test.
     * @param testDescription            The description of the test.
     * @param testSupportedPlatforms     The supported platforms for the test.
     * @param testInputArguments         The input arguments for the test.
     * @param testExecutor               The executor for the test.
     * @param testDependencyExecutorName The name of the dependency executor.
     * @param testDependencies           The dependencies of the test.
     */
    public AtomicRedTeam(String testName, String testGuid, String testDescription, String[] testSupportedPlatforms,
                         String[] testInputArguments, String[] testExecutor, String testDependencyExecutorName,
                         String[] testDependencies) {
        super();
        this.testName = testName;
        this.testGuid = testGuid;
        this.testDescription = testDescription;
        this.testSupportedPlatforms = testSupportedPlatforms;
        this.testInputArguments = testInputArguments;
        this.testExecutor = testExecutor;
        this.testDependencyExecutorName = testDependencyExecutorName;
        this.testDependencies = testDependencies;
    }

    /**
     * Constructor for the AtomicRedTeam class.
     *
     * @param testNumber                 The number of the test.
     * @param testName                   The name of the test.
     * @param testGuid                   The auto-generated GUID for the test.
     * @param testDescription            The description of the test.
     * @param testSupportedPlatforms     The supported platforms for the test.
     * @param testInputArguments         The input arguments for the test.
     * @param testExecutor               The executor for the test.
     * @param testDependencyExecutorName The name of the dependency executor.
     * @param testDependencies           The dependencies of the test.
     */
    public AtomicRedTeam(int testNumber, String testName, String testGuid, String testDescription,
                         String[] testSupportedPlatforms, String[] testInputArguments, String[] testExecutor,
                         String testDependencyExecutorName, String[] testDependencies) {
        super();
        this.testNumber = testNumber;
        this.testName = testName;
        this.testGuid = testGuid;
        this.testDescription = testDescription;
        this.testSupportedPlatforms = testSupportedPlatforms;
        this.testInputArguments = testInputArguments;
        this.testExecutor = testExecutor;
        this.testDependencyExecutorName = testDependencyExecutorName;
        this.testDependencies = testDependencies;
    }

    // Getters and Setters
    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public String getTestName() {
        return testName;
    }

    public String getTestGuid() {
        return testGuid;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public String[] getTestSupportedPlatforms() {
        return testSupportedPlatforms;
    }

    public String[] getTestInputArguments() {
        return testInputArguments;
    }

    public String[] getTestExecutor() {
        return testExecutor;
    }

    public String getTestDependencyExecutorName() {
        return testDependencyExecutorName;
    }

    public String[] getTestDependencies() {
        return testDependencies;
    }

    /**
     * Downloads Atomic Red Team data.
     *
     * @throws URISyntaxException If there is an error in the URI syntax.
     */
//    @Override
//    public void downloadData() throws URISyntaxException {
//        final String ATOMIC_URL = "https://raw.githubusercontent.com/redcanaryco/atomic-red-team/master/atomics/Indexes/";
//        final String ATOMIC_DIRECTORY = "./data/atomic";
//        final String[] ATOMIC_FILES = {"index.yaml"};
//
//        dataGetter atomicDownloader = new dataGetter(ATOMIC_URL, ATOMIC_DIRECTORY);
//        atomicDownloader.downloadData("atomic-all.yaml");
//
//        final String YAML_FILE_PATH = "./data/atomic/atomic-all.yaml";
//        final String JSON_FILE_PATH = "./data/atomic/atomic-all.json";
//
//        YamlToJsonConverter converter = new YamlToJsonConverter(YAML_FILE_PATH, JSON_FILE_PATH);
//        converter.convert();
//    }

    /**
     * Exports Atomic Red Team data to an Excel file.
     *
     * @throws IOException If there is an I/O error.
     */
    public void exportExcel() throws IOException {
        final String JSON_FILE_PATH = "./data/atomic/atomic-all.json";
        final String EXCEL_FILE_PATH = "./data/atomic/atomic-all.xlsx";
        JsonToExcelConverter exporter = new JsonToExcelConverter(JSON_FILE_PATH, EXCEL_FILE_PATH);
        exporter.export();
    }



}
