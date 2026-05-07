package study.springbootdeveloperreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springbootdeveloperreview.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
}
