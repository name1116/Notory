package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.model.TempNote;
import com.pearl_castle.notory.service.NoteService;
import com.pearl_castle.notory.service.TempNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request/detail")
public class CollaboratorRequestController {

    private final TempNoteService tempNoteService;
    private final NoteService noteService;

    //공동 작업자 요청 처리. 수락/거절
    @PostMapping("/decision")
    public String acceptOrRejectNote(@RequestParam Long tempNoteId,@RequestParam String action, @RequestParam Long originNoteId) {

        TempNote tempNote = tempNoteService.findById(tempNoteId);

        if (tempNote != null) {
            Long noteId = tempNote.getOriginNote().getId();
            Note note = new Note();
            note.setTitle(tempNote.getTitle());
            note.setContent(tempNote.getContent());

            if("accept".equals(action)) {
                noteService.updateNote(noteId, note);
                tempNoteService.delete(tempNoteId);
            }else if ("reject".equals(action)) {
                tempNoteService.delete(tempNoteId);
            }
        }
        return "redirect:/home/list/owner/"+originNoteId;
    }


}
