package com.udacity.jwdnd.course1.cloudstorage.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotesHomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "add-new-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    @FindBy(id = "note-save-changes-button")
    private WebElement noteSaveChangesButton;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(xpath = "//*[@id=\"userTable\"]/tbody/tr") //#userTable > tbody > tr
    private WebElement firstNote;

    public NotesHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickOnLogout() {
        logoutButton.click();
    }

    public void clickOnNotesTab() {
        this.notesTab.click();
    }

    public void clickOnAddNewNote() {
        this.addNoteButton.click();
    }

    public void clickOnEditNote() {
        this.editNoteButton.click();
    }

    public void clickOnDeleteNote() {
        this.deleteNoteButton.click();
    }

    public void createOrEditNote(String title, String description) {
        this.noteTitle.clear();
        this.noteTitle.sendKeys(title);

        this.noteDescription.clear();
        this.noteDescription.sendKeys(description);

        this.noteSaveChangesButton.click();
    }

    public String getNoteTitle() {
        return this.firstNote.findElement(By.id("displayed-note-title")).getText();
    }

    public String getNoteDescription() {
        return this.firstNote.findElement(By.id("displayed-note-desc")).getText();
    }
}