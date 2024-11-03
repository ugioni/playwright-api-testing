package API.Utils;

import com.microsoft.playwright.Playwright;

public class Core {

    public Playwright playwright;

    public void iniciarTestes() {
        playwright = Playwright.create();
    }

    public void finalizarTestes() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
