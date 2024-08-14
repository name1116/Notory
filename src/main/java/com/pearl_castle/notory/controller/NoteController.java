package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.model.Collaborator;
import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.model.TempNote;
import com.pearl_castle.notory.service.CollaboratorService;
import com.pearl_castle.notory.service.MemberService;
import com.pearl_castle.notory.service.NoteService;
import com.pearl_castle.notory.service.TempNoteService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class NoteController {
    private final NoteService noteService;
    private final MemberService memberService;
    private final CollaboratorService collaboratorService;
    private final TempNoteService tempNoteService;

    /**
     * 로그인한 사용자가 접근가능한 게시글 목차
     */
    @GetMapping("/list")
    public String list(Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);
        }

        // 로그인한 사용자 정보
        Member member = memberService.findByMemberName(username);

        // 사용자가 소유하거나 공동 작업자로 있는 노트 가져오기
        List<Note> notes = new ArrayList<>();
        // 내가 소유한 노트
        for (Note note : member.getNotes()) {
            notes.add(note);
        }
        // 내가 포함 되어 있는(구성원) 노트
        List<Note> workerNotes = collaboratorService.getCollaboratorNoteByMemberId(member.getId());
        if (workerNotes != null && !workerNotes.isEmpty()) {
            for (Note note : workerNotes) {
                notes.add(note);
            }
        }
        model.addAttribute("notes", notes);

        return "list";
    }

    /**
     * 글 수정
     */
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        try {
            Note note = noteService.findById(id);
            model.addAttribute("note", note);
        } catch (IllegalStateException e) {

        }
        return "write";
    }
    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note note, Authentication authentication, Model model) {
        String username = "";
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);

            Long ownerId = noteService.getOwnerId(id);
            Member member = memberService.findByMemberName(username);

            if(ownerId == member.getId()) {
                noteService.updateNote(id, note);
            } else {
                tempNoteService.addTempNote(member, note); //노트ID, 멤버ID, 노트 통짜
            }
        }
        return "redirect:/home/list"; // 글 목록으로 리다이렉트
    }

    /**
     * 글 추가
     */
    @GetMapping("/add")
    public String showAddForm(@RequestParam(value = "error", required = false) String error, Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);
        }

        model.addAttribute("note", new Note());

        if(error != null) {
            int maxLength = (Integer) model.asMap().get("maxLength");
            model.addAttribute("error", "입력된 내용이 허용된 길이를 초과했습니다. (" + maxLength + "자까지 입력 가능합니다.)");
        }
        return "write"; // write.html
    }
    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        Member member = memberService.findByMemberName(authentication.getName());
        note.setOwner(member);
        String accountType = member.getRole();

        int maxLength = 100; // Standard (Default)
        if(accountType.equals("premium")) {
            maxLength = 2000;
        }

        if (note.getContent().length() > maxLength) {
            // 길이 초과 시 에러 메시지를 모델에 추가하고 폼으로 다시 이동
            redirectAttributes.addFlashAttribute("maxLength", maxLength);
            return "redirect:/home/add?error";  // contentForm은 다시 작성하는 페이지 이름
        }

        noteService.save(note);
        return "redirect:/home/list"; // 글 목록으로 리다이렉트
    }

    @PostMapping("/delete/{id}")
    public String deleteNoteById(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return "redirect:/home/list"; // 글 목록 페이지로 리다이렉트
    }

    /**
     * 소유한 글 상세
     */
    @GetMapping("/list/owner/{id}")
    public String viewOwnerDetails(@PathVariable Long id, Model model) {
        Note note = noteService.findById(id);  // noteService를 통해 해당 id의 노트를 가져옵니다.

        List<TempNote> tempNotes = tempNoteService.findByNoteId(note.getId());

        if(tempNotes != null && !tempNotes.isEmpty()) {
            model.addAttribute("tempNotes", tempNotes);
        }

        return "owner";  // "owner"는 owner.html을 가리킵니다.
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/list/detail/{id}")
    public String getNoteDetail(@PathVariable("id") Long id, Model model) {
        Note note = noteService.findById(id);
        model.addAttribute("note", note);
        return "detail";
    }
}
