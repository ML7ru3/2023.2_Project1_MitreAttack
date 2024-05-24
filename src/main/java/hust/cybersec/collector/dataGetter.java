package hust.cybersec.collector;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class dataGetter {
    public static void main(String args[]) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.github.com/repos/redcanaryco/atomic-red-team/contents/atomics/Indexes/index.yaml"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.err.println("Failed to fetch data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch data.");
            e.printStackTrace();
        }

    }
}