package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.service.MemberService;
import com.pearl_castle.notory.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/role")
public class BuyRoleController {
    private final MemberService memberService;

    @GetMapping("/buy")
    public String buyRoleForm(Model model) {
        return "buyrole";
    }

    @PostMapping("/buy")
    public String buyRole(@RequestParam String role, Authentication authentication) {
        Member member = memberService.findByMemberName(authentication.getName());
        if (member != null) memberService.updateRole(member.getId(), role);

        return "redirect:/home";
    }
}
