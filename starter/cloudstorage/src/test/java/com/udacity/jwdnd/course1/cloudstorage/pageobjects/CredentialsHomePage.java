package com.udacity.jwdnd.course1.cloudstorage.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialsHomePage {

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "add-new-cred-button")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-cred-button")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-cred-button")
    private WebElement deleteCredentialButton;

    @FindBy(id = "cred-save-changes-button")
    private WebElement credentialSaveChangesButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr")
    private WebElement firstCredential;

    public CredentialsHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickOnCredentialsTab() {
        this.credentialsTab.click();
    }

    public void clickOnAddNewCredential() {
        this.addCredentialButton.click();
    }

    public void clickOnEditCredential() {
        this.editCredentialButton.click();
    }

    public void clickOnDeleteCredential() {
        this.deleteCredentialButton.click();
    }

    public void createOrEditCredential(String url, String username, String password) {
        this.credentialUrl.clear();
        this.credentialUrl.sendKeys(url);

        this.credentialUsername.clear();
        this.credentialUsername.sendKeys(username);

        this.credentialPassword.clear();
        this.credentialPassword.sendKeys(password);

        this.credentialSaveChangesButton.click();
    }

    public String getEditCredentialModalPassword() {
        return this.credentialPassword.getAttribute("value");
    }

    public String getCredentialUrl() {
        return this.firstCredential.findElement(By.id("displayed-cred-url")).getText();
    }

    public String getCredentialUsername() {
        return this.firstCredential.findElement(By.id("displayed-cred-username")).getText();
    }

    public String getCredentialPassword() {
        return this.firstCredential.findElement(By.id("displayed-cred-password")).getText();
    }
}
