package API;

import API.Utils.Core;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import org.junit.jupiter.api.*;

import static io.qameta.allure.SeverityLevel.NORMAL;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RunApiTests extends Core {

    private String token = null;

    @BeforeAll
    void iniciar() {
        iniciarTestes();
        criarRequests("https://serverest.dev", 30000);
    }

    void reaizarLogin() {
        String dados = "{\"email\": \"henrique@gmail.com\", \"password\": \"123456789\"}";
        APIResponse resposta = request.post("/login", RequestOptions.create().setData(dados));
        validarResposta(resposta, 200);
        token = resposta.text().substring(70).replace("\"", "").replace("}", "").trim();
    }

    @BeforeEach
    void login() {
        reaizarLogin();
        criarRequests("https://serverest.dev", 30000, token);
    }

    @Test
    @DisplayName("POST - Cadastrar Produto")
    @Description("POST - Cadastrar Produto")
    @Severity(NORMAL)
    @Owner("Ugioni")
    void testCadastroProduto() {
        String dados = "{\"nome\": \"pinheirinho de natal\",\"preco\": 520,\"descricao\": \"produto\",\"quantidade\": 10}";
        APIResponse resposta = request.post("/produtos", RequestOptions.create().setData(dados));
        validarResposta(resposta, 201);
    }

    @AfterAll
    void finalizar() {
        encerrarRequests();
        finalizarTestes();
    }
}