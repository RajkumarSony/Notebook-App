package com.notebook.app.controllers;

import com.notebook.app.models.Note;
import com.notebook.app.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public Note createNote(@RequestParam String title,
                           @RequestParam String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.createNoteForUser(username, title, content);
    }

    @GetMapping
    public List<Note> getAllNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.getAllNotesForUser(username);
    }

    @GetMapping("/{noteId}")
    public Note getNote(@PathVariable Long noteId,
                        @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.getNoteForUser(username, noteId);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestParam String title,
                           @RequestParam String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.updateNoteForUser(username, noteId, title, content);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        noteService.deleteNoteForUser(username, noteId);
    }
}
