import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    //api url
    private static final String POST_API_URL = "https://randomuser.me/api/?results=10&inc=gender,name,email,id&noinfo";
    
    public static void main(String[] args) throws  IOException, InterruptedException{
        
        //client
        HttpClient client = HttpClient.newHttpClient();
        
        //request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(POST_API_URL))
                .build();
        
        //send async request
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        System.out.println(response);
        
        //send sync request
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
}
