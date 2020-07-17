package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pageobjects.FilesHomePage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pageobjects.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilesCrudTests {

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
    void testFileUploadDelete() {

        //upload
        driver.get(baseUrl + "/home");
        FilesHomePage filesHomePage = new FilesHomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));

        filesHomePage.uploadFile("/Users/kelsey/Desktop/IntelliJKeymap.pdf");
        Assertions.assertEquals(baseUrl + "/files", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSuccessful());

        resultPage.clickToReturnHomeSuccess();

        driver.get(baseUrl + "/home");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileTable")));

        Assertions.assertTrue(driver.getPageSource().contains("IntelliJKeymap.pdf"));

        //view
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);

        driver.get(baseUrl + "/home");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("view-file-button"))).click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //with help from https://stackoverflow.com/questions/36468876/test-if-a-file-has-been-downloaded-selenium-c-google-chrome
        File getDownloadedFile = getLatestFileFromDir("/Users/kelsey/Downloads");
        String fileName = null;
        if (getDownloadedFile != null) {
            fileName = getDownloadedFile.getName();
        }
        Assertions.assertEquals("IntelliJKeymap.pdf", fileName);

        //delete
        driver.get(baseUrl + "/home");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-file-button"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("home-success"))).click();

        driver.get(baseUrl + "/home");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileTable")));

        Assertions.assertFalse(driver.getPageSource().contains("IntelliJKeymap.pdf"));

    }

    private File getLatestFileFromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }
}
