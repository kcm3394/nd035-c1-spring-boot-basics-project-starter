package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pageobjects.NotesHomePage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpLoginFlowTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String baseUrl;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.baseUrl = "http://localhost:" + this.port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUnauthorizedPageRequests() {
        driver.get(baseUrl + "/home");
        Assertions.assertNotEquals((baseUrl + "/home"), driver.getCurrentUrl());

        driver.get(baseUrl + "/notes");
        Assertions.assertNotEquals((baseUrl + "/notes"), driver.getCurrentUrl());
    }

    @Test
    void testSignupLoginLogout() {
        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("John", "Smith", "user", "password");

        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user", "password");

        driver.get(baseUrl + "/home");
        NotesHomePage notesHomePage = new NotesHomePage(driver);
        notesHomePage.clickOnLogout();

        //home page no longer accessible
        driver.get(baseUrl + "/home");
        String expectedUrl = baseUrl + "/login";
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl());
    }
}
