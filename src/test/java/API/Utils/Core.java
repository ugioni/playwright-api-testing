package API.Utils;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Core {

    public Playwright playwright;
    public APIRequestContext request;

    public void iniciarTestes() {
        playwright = Playwright.create();
    }

    public void finalizarTestes() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public void criarRequests(String baseUrl, Integer timeout) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl)
                .setIgnoreHTTPSErrors(true)
                .setTimeout(timeout)
                .setExtraHTTPHeaders(headers));
    }

    public void criarRequests(String baseUrl, Integer timeout, String token) {
        encerrarRequests();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token);
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl)
                .setIgnoreHTTPSErrors(true)
                .setTimeout(timeout)
                .setExtraHTTPHeaders(headers));
    }

    public void encerrarRequests() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }

    public void validarResposta(APIResponse resposta, Integer statusCode) {
        assertEquals(resposta.status(), statusCode);
        assertTrue(resposta.ok(), resposta.text());
    }
}