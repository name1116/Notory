package com.pearl_castle.notory.service;

import com.pearl_castle.notory.model.Collaborator;
import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.repository.CollaboratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollaboratorService {
    private final CollaboratorRepository collaboratorRepository;
    private final MemberService memberService;
    private final NoteService noteService;

    public void save(String memberName, Long noteId) {
        Member member = memberService.findByMemberName(memberName);
        Note note = noteService.findById(noteId);

        if (member.getMemberName() == note.getOwner().getMemberName()) throw new IllegalStateException("소유주는 초대할 수 없습니다.");
        if (member == null) throw new IllegalStateException("사용자 정보가 없습니다.");
        if (note == null) throw new IllegalStateException("게시글 정보가 없습니다.");

        Collaborator collaborator = new Collaborator();
        collaborator.setMember(member);
        collaborator.setNote(note);

        Optional<Collaborator> optionalCollaborator = collaboratorRepository.findByMemberAndNote(member, note);
        if (optionalCollaborator.isPresent()) throw new IllegalStateException("이미 초대된 상태입니다.");

        collaboratorRepository.save(collaborator);
    }

    public List<Note> getCollaboratorNoteByMemberId(Long id) {
        List<Collaborator> collaborators = collaboratorRepository.findByMemberId(id);
        if (collaborators.isEmpty()) return null;

        List<Note> notes = new ArrayList<>();
        for (Collaborator collaborator : collaborators) {
            notes.add(collaborator.getNote());
        }
        return notes;
    }

    public List<Collaborator> getListByMemberId(Long id) {
        return collaboratorRepository.findByMemberId(id);
    }

    public Collaborator findByNoteId(Long id) {
        Optional<Collaborator> collaborator = collaboratorRepository.findByNoteId(id);
        if (collaborator.isEmpty()) throw new IllegalStateException("조회되는 공동작업자가 없습니다.");

        return collaborator.get();
    }
}
