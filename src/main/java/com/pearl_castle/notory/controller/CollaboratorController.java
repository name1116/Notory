package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.model.Collaborator;
import com.pearl_castle.notory.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/collaborator")
public class CollaboratorController {
    private final CollaboratorService collaboratorService;

    /**
     * 글 공동 작업자로 초대
     */
    @GetMapping("/invite/{noteId}")
    public String showInviteForm(@PathVariable Long noteId, @RequestParam(value = "error", required = false) String error, Model model, Authentication authentication) {
        try {
            if (error != null) {
                String errorMessage = (String) model.asMap().get("errorMessage");
                model.addAttribute("errorMessage", errorMessage);
            }
            String username = "";
            if (authentication != null && authentication.isAuthenticated()) {
                username = authentication.getName();
                model.addAttribute("username", username);
            }
            if (noteId == null || noteId < 0) return "redirect:/home/list"; // 글 목록으로 리다이렉트

            // note 소유주가 아니면 페이지 접근 불가
            Collaborator collaborator = collaboratorService.findByNoteId(noteId);
            if (!collaborator.getNote().getOwner().getMemberName().equals(username)) {
                return "redirect:/home/list"; // 글 목록으로 리다이렉트
            }

            model.addAttribute("noteId", noteId);

        }  catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "invite";
    }

    @PostMapping("/invite/{noteId}")
    public String inviteCollaborator(@PathVariable Long noteId, @RequestParam String memberName, RedirectAttributes redirectAttributes) {
        try {
            // 공동작업자 DB 저장
            collaboratorService.save(memberName, noteId);
            return "redirect:/home/update/" + noteId;
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/collaborator/invite/" + noteId + "?error";
        }
    }
}
