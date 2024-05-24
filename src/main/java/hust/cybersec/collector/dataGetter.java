package hust.cybersec.collector;


import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class dataGetter {
    static void retrieveData(String url, String filename) throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
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
                    downloadData(downloadUrl, filename);
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

    static void downloadData(String downloadUrl, String filename) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Failed to fetch data.");
        }
    }

//
//    public static void main(String[] args) {
////        FORM:
////        try {
////            retrieveData("https://api.github.com/repos/redcanaryco/atomic-red-team/contents/atomics/Indexes/index.yaml", "index.yaml");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }
}
