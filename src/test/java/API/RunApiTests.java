package API;

import API.Utils.Core;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RunApiTests extends Core {


    private APIRequestContext request;

    private String token = " ";


    void criarAPIRequestContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://serverest.dev")
                .setIgnoreHTTPSErrors(true)
                .setTimeout(30000)
                .setExtraHTTPHeaders(headers));
    }


    @BeforeAll
    void iniciar() {
        iniciarTestes();
        criarAPIRequestContext();
        //createTestRepository();
        reaizarLogin();
    }

    String reaizarLogin() {
        String dados = "{\"email\": \"henrique@gmail.com\", \"password\": \"123456789\"}";
        System.out.println("----->>>> " + dados);
        APIResponse login =
                request.post("/login",
                        RequestOptions.create().setData(dados));

        System.out.println("----->>>> " + login.text());

        assertTrue(login.ok(), login.text());
        return login.text();
    }

    @BeforeEach
    void login() {
        reaizarLogin();
    }


    @Test
    void shouldCreateBugReport() {
        Map<String, String> data = new HashMap<>();
        data.put("title", "[Bug] report 1");
        data.put("body", "Bug description");

        APIResponse newIssue = request.post("/repos/" + "/" + "/issues", RequestOptions.create().setData(data));
        assertTrue(newIssue.ok());

        APIResponse issues = request.get("/repos/" + "/" + "/issues");
        assertTrue(issues.ok());
        JsonArray json = new Gson().fromJson(issues.text(), JsonArray.class);
        JsonObject issue = null;
        for (JsonElement item : json) {
            JsonObject itemObj = item.getAsJsonObject();
            if (!itemObj.has("title")) {
                continue;
            }
            if ("[Bug] report 1".equals(itemObj.get("title").getAsString())) {
                issue = itemObj;
                break;
            }
        }
        assertNotNull(issue);
        assertEquals("Bug description", issue.get("body").getAsString(), issue.toString());
    }


    void encerrarAPIRequestContext() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }

    @AfterAll
    void finalizar() {
        //  deleteTestRepository();
        encerrarAPIRequestContext();
        finalizarTestes();
    }
}