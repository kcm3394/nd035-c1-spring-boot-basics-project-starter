package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateNotes(Authentication authentication, @ModelAttribute Note note, Model model) {
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        logger.info("Passed in note title is " + note.getTitle());
        logger.info("Passed in note description is " + note.getDescription());

        if (this.noteService.getNoteByNoteId(note.getNoteId(), userId) != null) {
            this.noteService.updateNote(note.getTitle(), note.getDescription(), note.getNoteId(), userId);
        } else {
            this.noteService.createNote(new Note(null, note.getTitle(), note.getDescription(), userId));

        }
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("resultSuccess", true);
        return "result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model) {
        Integer userId = this.userService.getUser(authentication.getName()).getUserId();
        int result = this.noteService.deleteNote(noteId, userId);
        if (result > 0) {
            model.addAttribute("resultSuccess", true);
        } else {
            model.addAttribute("resultError", "Note ID does not exist.");
        }
        return "result";
    }
}
