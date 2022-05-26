import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.InvalidPathException;
import java.util.Iterator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

class MainTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String POST_API_URL = "https://randomuser.me/api/?results=10&inc=gender,name,email,id&noinfo";

    @Test
    void printUserTestJson2() throws IOException, URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource("userTest2.json");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        String json = new String(bytes);
        JsonNode node = objectMapper.readTree(json);

        System.out.println(node.toPrettyString());
    }

    @Test
    void loopThroughUserTestJson2() throws IOException, URISyntaxException {
        try {
            URL resource = Main.class.getClassLoader().getResource("userTest2.json");
            byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
            String json = new String(bytes);
            JsonNode node = objectMapper.readTree(json);

            if (node.isArray()) {
                Iterator<JsonNode> var = node.iterator();
                while (var.hasNext()) {
                    JsonNode jsonNode = var.next();
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
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void printUsersFromAPI() throws IOException, URISyntaxException, InvalidPathException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(POST_API_URL))
                .build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        JsonNode node = objectMapper.readTree(response);
        System.out.println(node.toPrettyString());
    }

    @Test
    void loopThroughUserFromAPI() throws JsonProcessingException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(POST_API_URL))
                .build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        
        JSONParser parse = new JSONParser();
        JSONObject jsonObject = (JSONObject) parse.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("results");
        JsonNode node = objectMapper.readTree(String.valueOf(jsonArray));

        if (node.isArray()) {
            Iterator<JsonNode> var = node.iterator();
            while (var.hasNext()) {
                JsonNode jsonNode = var.next();
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