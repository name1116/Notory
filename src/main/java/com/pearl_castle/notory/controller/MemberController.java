package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.model.Note;
import com.pearl_castle.notory.service.CollaboratorService;
import com.pearl_castle.notory.service.MemberService;
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
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final CollaboratorService collaboratorService;

    /**
     * 마이페이지
     */
    // Home -> mypage
    @GetMapping("/mypage")
    public String myPage(Model model, Authentication authentication) {
        String username = "";
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);
        }
        Member member = memberService.findByMemberName(username);
        model.addAttribute("member", member);

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

        return "mypage";
    }


    /**
     * 멤버 검색
     */
    @GetMapping("/search")
    public String search(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            String errorMessage = (String) model.asMap().get("errorMessage");
            model.addAttribute("errorMessage", errorMessage);
        }
        return "invite";
    }

    @PostMapping("/search")
    public String searchMember(@RequestParam String memberName, @RequestParam Long noteId, RedirectAttributes redirectAttributes) {
        try {
            Member member = memberService.findByMemberName(memberName);
            redirectAttributes.addFlashAttribute("member", member);
            return "redirect:/collaborator/invite/" + noteId;

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/collaborator/invite/"+noteId+"?error";
        }
    }
}
