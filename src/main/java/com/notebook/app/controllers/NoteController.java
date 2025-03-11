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

    @PostMapping
    public Note createNote(@RequestBody Note note,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.createNoteForUser(username, note.getTitle(), note.getContent());
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody Note note,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.updateNoteForUser(username, noteId, note.getTitle(), note.getContent());
    }

    @DeleteMapping("/{noteId}")
    public String deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("User Name: " + username);
        return noteService.deleteNoteForUser(username, noteId);
    }
}
