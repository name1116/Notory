package com.pearl_castle.notory.controller;

import com.pearl_castle.notory.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    /**
     * Main 페이지
     */
    @GetMapping("/")
    public String basic() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "home";
    }

    /**
     * 회원 가입
     */
    @GetMapping("/join")
    public String signUpForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "가입에 실패했습니다. 다시 시도해주세요.");
        }
        return "join";
    }

    @PostMapping("/join")
    public String signUp(@RequestParam String memberName, @RequestParam String password) {
        try {
            memberService.joinMember(memberName, password);
            return "redirect:/login";
        } catch (IllegalStateException e) {
            return "redirect:/join?error";
        }
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "로그인에 실패했습니다. 다시 시도해주세요.");
        }
        return "login";
    }

}
