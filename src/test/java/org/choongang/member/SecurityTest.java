package org.choongang.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test // 요청헤더에 토큰을 담아서 보내야 한다 // 토큰안담으면 응답코드 403
    void test1() throws Exception {
        mockMvc.perform(post("/member/join")
                        .with(csrf().asHeader()) // 토큰 요청에 실어서 보내기
                .param("email", "user02@test.org"))
                .andDo(print());
    }

    @Test
    @WithMockUser // 로그인 상태 유지해주는 애노테이션
    void test2() throws Exception {
        mockMvc.perform(get("/mypage"))
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "user01@test.org", authorities = "ADMIN")
    void test3() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print());
    }
}
