package hust.cybersec.collector;


import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import hust.cybersec.conversion.YamlToJsonConverter;
import org.json.JSONObject;

public class dataGetter {

    private String URL;
    private String filename;

    public dataGetter(String URL, String filename){
        this.URL = URL;
        this.filename = filename;
    }

    public void retrieveData() throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody);
                String downloadUrl = jsonObject.getString("download_url");
                try {
                    downloadData(downloadUrl);
                } catch (IOException e) {
                    System.err.println("oh no");
                }
            } else {
                System.err.println("Failed to fetch data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch data.");
            e.printStackTrace();
        }
    }

    public void downloadData(String downloadUrl) throws IOException {
        String directoryPath = "src/main/java/hust/cybersec/data/";
        if (!directoryPath.endsWith("/") && !directoryPath.endsWith("\\")) {
            directoryPath += System.getProperty("file.separator");
        }


        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(directoryPath + filename)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Failed to fetch data.");
        }
    }


//    public static void main(String[] args) {
//        final String[] MITRE_URL = {"https://api.github.com/repos/mitre-attack/attack-stix-data/contents/enterprise-attack/enterprise-attack.json",
//                "https://api.github.com/repos/mitre-attack/attack-stix-data/contents/ics-attack/ics-attack.json",
//                "https://api.github.com/repos/mitre-attack/attack-stix-data/contents/mobile-attack/mobile-attack.json"};
//        final String[] NAME_FILE = {"enterprise-attack.json",
//                "ics-attack.json",
//                "mobile-attack.json"};
//
//        for (int i = 0; i < 3; i++){
//            dataGetter mitreRetriever = new dataGetter(MITRE_URL[i], NAME_FILE[i]);
//            try {
//                mitreRetriever.retrieveData();
//            } catch( Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        final String ATOMIC_URL = "https://api.github.com/repos/redcanaryco/atomic-red-team/contents/atomics/Indexes/index.yaml";
//        final String NAME_FILE = "index.yaml";
//        dataGetter atomicRetriever = new dataGetter(ATOMIC_URL, NAME_FILE);
//
//        try {
//            atomicRetriever.retrieveData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
