package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        logger.info("Passed in note id is " + note.getNoteId());
        logger.info("Passed in note title is " + note.getTitle());
        logger.info("Passed in note description is " + note.getDescription());
        logger.info("Passed in note user id is " + note.getUserId());

        if (this.noteService.getNoteByNoteId(note.getNoteId()) != null) {
            noteService.updateNote(note.getTitle(), note.getDescription(), note.getNoteId());
        } else {
            noteService.createNote(new Note(null, note.getTitle(), note.getDescription(), userId));

        }
        model.addAttribute("notes", this.noteService.getNotesByUserId(userId));
        model.addAttribute("resultSuccess", true);
        return "result";
    }

    @PostMapping("/delete")
    public String deleteNote(@RequestParam Integer noteId, Model model) {
        if (noteId > 0) {
            //delete
        }
        return "result";
    }
}
