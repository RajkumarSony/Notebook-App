package com.notebook.app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notebook.app.models.Note;
import com.notebook.app.repositories.NoteRepository;
import com.notebook.app.services.NoteService;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note createNoteForUser(String username, String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setOwnerUserName(username);
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteForUser(String username, Long noteId) {
        return noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @Override
    public Note updateNoteForUser(String username, Long noteId, String title, String content) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    public String deleteNoteForUser(String username, Long noteId) {
        noteRepository.deleteById(noteId);
        return "Note deleted successfully";
    }

    @Override
    public List<Note> getAllNotesForUser(String username) {
        return noteRepository.findByOwnerUserName(username);
    }
}
