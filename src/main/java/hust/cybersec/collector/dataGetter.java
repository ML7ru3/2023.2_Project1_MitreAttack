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

    private final String URL;
    private final String filename;

    public dataGetter(String URL, String filename){
        this.URL = URL;
        this.filename = filename;
    }

    public void retrieveData() throws IOException {
        System.out.println("Retrieving data from " + URL);
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
                System.out.println("Retrieve data successfully");
                String responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody);
                String downloadUrl = jsonObject.getString("download_url");
                try {
                    downloadData(downloadUrl);
                } catch (IOException e) {
                    System.err.println("oh no");
                }
            } else {
                System.err.println(STR."Failed to fetch data. Status code: \{response.statusCode()}");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch data.");
        }
    }

    public void downloadData(String downloadUrl) throws IOException {
        String directoryPath = "src/main/java/hust/cybersec/data/";
        if (!directoryPath.endsWith("/") && !directoryPath.endsWith("\\")) {
            directoryPath += System.getProperty("file.separator");
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(directoryPath + filename)) {
            System.out.println("Downloading data...");
            byte dataBuffer[] = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Failed to fetch data.");
        }

        System.out.println("Download completed");
        System.out.println("-----------------------------------------------");
        System.out.println();
    }

}
