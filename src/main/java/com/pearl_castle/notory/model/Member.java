package com.pearl_castle.notory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_name", nullable = false, unique = true)
    private String memberName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "Standard";

    // Member : Note = 1 : n
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    // Member : Collaborator = 1 : n
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Collaborator> collaborators = new ArrayList<>();

    // Member : TempNote = 1 : n
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<TempNote> tempNotes = new ArrayList<>();
}
