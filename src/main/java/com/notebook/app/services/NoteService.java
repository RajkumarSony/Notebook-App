package com.notebook.app.services;

import com.notebook.app.models.Note;

import java.util.List;


public interface NoteService {

    Note createNoteForUser(String username, String title, String content);

    Note getNoteForUser(String username, Long noteId);

    Note updateNoteForUser(String username, Long noteId, String title, String content);

    String deleteNoteForUser(String username, Long noteId);

    List<Note> getAllNotesForUser(String username);
}