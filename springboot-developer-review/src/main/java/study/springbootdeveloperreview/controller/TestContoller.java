package study.springbootdeveloperreview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springbootdeveloperreview.domain.Member;
import study.springbootdeveloperreview.service.TestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestContoller {

    private final TestService testService;

    @GetMapping("/test")
    public List<Member> getALlMembers() {
        return testService.getAllMembers();
    }
}
