package com.pearl_castle.notory.service;

import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.repository.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Transactional
    public void updateNote(Long id, Note note) {
        if (!noteRepository.existsById(id)) {
            throw new RuntimeException("Note not found for id :: " + id);
        }
        noteRepository.updateNote(id, note.getTitle(), note.getContent());
    }


    public List<Note> findAllByUserId(Long memberId) {
        List<Note> notes = noteRepository.findAllByOwnerId(memberId);

        if (notes.isEmpty()) return null;

        return notes;
    }

    public Note findById(Long id) {
        Note noteOptional = noteRepository.findById(id).orElse(null);
        if (noteOptional == null) {
            throw new IllegalStateException("게시글이 없습니다.");
        }

        return noteOptional;
    }

    @Transactional
    public void deleteNoteById(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new RuntimeException("Note not found for id :: " + id);
        }
        noteRepository.deleteById(id);
    }

    public Long getOwnerId(Long id) {
        return noteRepository.getOwnerId(id);
    }
}
