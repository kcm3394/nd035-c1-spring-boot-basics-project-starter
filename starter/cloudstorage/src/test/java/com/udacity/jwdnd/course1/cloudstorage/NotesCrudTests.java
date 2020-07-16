package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pageobjects.NotesHomePage;
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
public class NotesCrudTests {

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
    void testNoteCreateEditDelete() {

        //create
        driver.get(baseUrl + "/home");
        NotesHomePage notesHomePage = new NotesHomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notesHomePage.clickOnNotesTab();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-new-note-button")));

        notesHomePage.clickOnAddNewNote();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-save-changes-button")));

        notesHomePage.createOrEditNote("New Note", "This is a note");
        Assertions.assertEquals(baseUrl + "/notes", driver.getCurrentUrl());

        ResultPage resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSuccessful());

        resultPage.clickToReturnHomeSuccess();

        driver.get(baseUrl + "/home");
        notesHomePage = new NotesHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notesHomePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        String firstNoteTitle = notesHomePage.getNoteTitle();
        String firstNoteDesc = notesHomePage.getNoteDescription();
        Assertions.assertEquals("New Note", firstNoteTitle);
        Assertions.assertEquals("This is a note", firstNoteDesc);

        //edit
        notesHomePage.clickOnEditNote();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-save-changes-button")));

        notesHomePage.createOrEditNote("Changed Note", "This is a changed note");
        resultPage = new ResultPage(driver);
        resultPage.clickToReturnHomeSuccess();

        driver.get(baseUrl + "/home");
        notesHomePage = new NotesHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notesHomePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        String changedNoteTitle = notesHomePage.getNoteTitle();
        String changedNoteDesc = notesHomePage.getNoteDescription();
        Assertions.assertEquals("Changed Note", changedNoteTitle);
        Assertions.assertEquals("This is a changed note", changedNoteDesc);

        //delete
        notesHomePage.clickOnDeleteNote();
        driver.get(baseUrl + "/home");
        notesHomePage = new NotesHomePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        notesHomePage.clickOnNotesTab();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

        Assertions.assertFalse(driver.getPageSource().contains("Changed Note"));
    }
}
