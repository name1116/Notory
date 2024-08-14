package com.pearl_castle.notory.repository;

import com.pearl_castle.notory.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberName(String memberName);
    Optional<Member> findByMemberName(String memberName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE member SET role = :role WHERE id = :userId", nativeQuery = true)
    void updateRole(@Param("userId") Long userId, @Param("role") String role);

}
