package com.pearl_castle.notory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class TempNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // TempNote : Member = n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    // TempNote : Note = n : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note originNote;

    @Column(nullable = false)
    private LocalDateTime cur_date; // 서버에서 만든시간을 DB에 넣어주는 형태

    @PrePersist // 저장되기 전에 지정해서 넣기
    protected void onCreate() {
        cur_date = LocalDateTime.now();
    }
}
