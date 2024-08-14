package com.pearl_castle.notory.service;

import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void joinMember(String memberName, String password) {
        // memberName 중복 체크
        if(memberRepository.existsByMemberName(memberName)) {
            throw new IllegalStateException("중복 아이디 입니다. 다른 아이디를 설정해주세요.");
        }
        // Member 등록
        Member member = new Member();
        member.setMemberName(memberName);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public Member findByMemberName(String name) {
        Optional<Member> memberOptional = memberRepository.findByMemberName(name);

        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return member;
        } else {
            throw new IllegalStateException("사용자 정보가 없습니다.");
        }
    }

    public void updateRole(Long id, String role) {
        memberRepository.updateRole(id, role);
    }
}
