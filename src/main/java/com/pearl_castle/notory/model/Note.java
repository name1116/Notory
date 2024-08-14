package com.pearl_castle.notory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Note : Member = 1 : n
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    // Note : Collaborator = 1 : n
    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<Collaborator> collaborators = new ArrayList<>();

    // Note : TempNote = 1 : n
    @OneToMany(mappedBy = "originNote", cascade = CascadeType.ALL)
    private List<TempNote> tempNotes = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime cur_date; // 서버에서 만든시간을 DB에 넣어주는 형태

    @PrePersist // 저장되기 전에 지정해서 넣기
    protected void onCreate() {
        cur_date = LocalDateTime.now();
    }
}
