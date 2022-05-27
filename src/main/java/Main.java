import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    //response
    private static String response = connectToApi();

    public static String connectToApi() {
        //client
        HttpClient client = HttpClient.newHttpClient();
        //api url with field inclusion specification
        String POST_API_URL = "https://randomuser.me/api/?results=10&inc=gender,name,email,id&noinfo";
        //send async request
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(POST_API_URL))
                .build();
        response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return response;
    }

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("results");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(String.valueOf(jsonArray));
        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                User user = objectMapper.treeToValue(jsonNode, User.class);
                System.out.println("Gender : " + user.getGender());
                Name name = user.getName();
                System.out.println("Name : " + name.getTitle() + " " + name.getFirst() + " " + name.getLast());
                System.out.println("Email : " + user.getEmail());
                Id id = user.getId();
                System.out.println("Id : name : " + id.getName() + ", value: " + id.getValue());
                System.out.println();
            }
        }
    }
}
