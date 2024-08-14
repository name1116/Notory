package com.pearl_castle.notory.repository;

import com.pearl_castle.notory.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByOwnerId(Long ownerId);
    @Modifying
    @Query("UPDATE Note n SET n.content = :content, n.title = :title WHERE n.id = :id")
    void updateNote(@Param("id") Long id, @Param("title") String title, @Param("content") String content);

    @Query("SELECT n.owner.id FROM Note n WHERE n.id = :id")
    Long getOwnerId(@Param("id") Long id);
}

