package com.pearl_castle.notory.repository;

import com.pearl_castle.notory.model.Collaborator;
import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    List<Collaborator> findByMemberId(Long id);
    List<Collaborator> findByNoteId(Long id);

    Optional<Collaborator> findByMemberAndNote(Member member, Note note);
}
