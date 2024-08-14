package com.pearl_castle.notory.repository;

import com.pearl_castle.notory.model.TempNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempNoteRepository extends JpaRepository<TempNote, Long> {
    @Query(value = "SELECT * FROM temp_note WHERE note_id = :id", nativeQuery = true)
    List<TempNote> findByNoteId(@Param("id") Long id);
}
