package com.udacity.jwdnd.course1.cloudstorage.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FilesHomePage {

    @FindBy(id = "upload-file-button")
    private WebElement uploadFileButton;

    @FindBy(id = "fileUpload")
    private WebElement chooseFile;

    @FindBy(id = "delete-file-button")
    private WebElement deleteFileButton;

    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr")
    private WebElement firstFile;

    public FilesHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void uploadFile(String filePath) {
        this.chooseFile.sendKeys(filePath);
        this.uploadFileButton.click();
    }

    public void deleteFile() {
        this.deleteFileButton.click();
    }

    public String getFileName() {
        return this.firstFile.findElement(By.id("displayed-file-name")).getText();
    }
}
