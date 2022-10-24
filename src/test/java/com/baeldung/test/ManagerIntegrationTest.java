package com.baeldung.test;

import com.baeldung.Application;
import com.baeldung.spring.SecSecurityConfig;
import com.baeldung.spring.TestIntegrationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { Application.class, TestIntegrationConfig.class, SecSecurityConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
class ManagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "manager@example.com", roles = "MANAGER")
    void shouldAccessManagementLink() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/management"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void shouldNotAcessManagementLinkForAdmin() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/management"));
        resultActions.andExpect(status().is3xxRedirection());
        resultActions.andExpect(redirectedUrl("/accessdenied"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void shouldNotAcessManagementLinkForUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/management"));
        resultActions.andExpect(status().is3xxRedirection());
        resultActions.andExpect(redirectedUrl("/accessdenied"));
    }

}
