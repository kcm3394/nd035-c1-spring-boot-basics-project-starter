package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pageobjects.CredentialsHomePage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialsCrudTests {

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

        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("John", "Smith", "user", "password");

        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user", "password");
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    void testCredentialCreateEditDelete() {

        //create
        driver.get(baseUrl + "/home");
        CredentialsHomePage credentialsHomePage = new CredentialsHomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-cred-button")));

        credentialsHomePage.clickOnAddNewCredential();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cred-save-changes-button")));

        credentialsHomePage.createOrEditCredential("test.com", "test", "test");
        Assertions.assertEquals(baseUrl + "/credentials", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSuccessful());

        resultPage.clickToReturnHomeSuccess();

        driver.get(baseUrl + "/home");
        credentialsHomePage = new CredentialsHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        String firstCredUrl = credentialsHomePage.getCredentialUrl();
        String firstCredUsername = credentialsHomePage.getCredentialUsername();
        String firstCredPassword = credentialsHomePage.getCredentialPassword();
        Assertions.assertEquals("test.com", firstCredUrl);
        Assertions.assertEquals("test", firstCredUsername);
        Assertions.assertNotEquals("test", firstCredPassword);

        //edit
        credentialsHomePage.clickOnEditCredential();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

        Assertions.assertEquals("test", credentialsHomePage.getEditCredentialModalPassword());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cred-save-changes-button")));

        credentialsHomePage.createOrEditCredential("newpage.com", "newuser", "newpassword");
        resultPage = new ResultPage(driver);
        resultPage.clickToReturnHomeSuccess();

        driver.get(baseUrl + "/home");
        credentialsHomePage = new CredentialsHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        String changedCredUrl = credentialsHomePage.getCredentialUrl();
        String changedCredUsername = credentialsHomePage.getCredentialUsername();
        String changedCredPassword = credentialsHomePage.getCredentialPassword();
        Assertions.assertEquals("newpage.com", changedCredUrl);
        Assertions.assertEquals("newuser", changedCredUsername);
        Assertions.assertNotEquals("newpassword", changedCredPassword);

        //delete
        credentialsHomePage.clickOnDeleteCredential();
        driver.get(baseUrl + "/home");
        credentialsHomePage = new CredentialsHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));

        credentialsHomePage.clickOnCredentialsTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

        Assertions.assertFalse(driver.getPageSource().contains("newpage.com"));

    }
}
