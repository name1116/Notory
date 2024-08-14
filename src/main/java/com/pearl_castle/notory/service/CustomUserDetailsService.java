package com.pearl_castle.notory.service;

import com.pearl_castle.notory.model.Member;
import com.pearl_castle.notory.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with member name: " + username));

        return new User(member.getMemberName(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
    }
}
