package com.pearl_castle.notory.service;

import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.model.TempNote;
import com.pearl_castle.notory.repository.TempNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempNoteService {
    private final TempNoteRepository tempNoteRepository;

    public TempNote findByNoteId(Long id) {
        return tempNoteRepository.findByNoteId(id);
    }

    public void addTempNote(Member member, Note note) {
        TempNote tempNote = new TempNote();
        tempNote.setOriginNote(note);
        tempNote.setWriter(member);
        tempNote.setTitle(note.getTitle());
        tempNote.setContent(note.getContent());

        tempNoteRepository.save(tempNote);
    }
}
