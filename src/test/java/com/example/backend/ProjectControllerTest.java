package com.example.backend;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.backend.entity.Project;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setupUser() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setRoles(List.of("ROLE_USER", "ROLE_ADMIN"));

        User user = new User();
        user.setUsername("user");
        user.setRoles(List.of("ROLE_USER"));

        when(userRepository.findByUsername("admin"))
            .thenReturn(Optional.of(adminUser));
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.of(user));
    }
    
    /* For the Project and User entities there are restrictions based on ROLES. User with 'ROLE_USER'
	 * is forbidden to add new Project. User with 'ROLE_ADMIN' may add Project.
	*/
	@Test
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	void whenAdminAddsProject_thenSuccess() throws Exception {
		Project project = new Project();
		project.setTitle("Test Project");
		project.setDescription("A sample project");
        
        mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(project)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Project"));
	}

    /* add project as a 'ROLE_USER' and fail */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenUserAddsProject_thenForbidden() throws Exception {
        Project project = new Project();
        project.setTitle("Test Project");

        mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(project)))
            .andDo(print())
        .andExpect(status().isForbidden());
    }

    /* get all projects as admin */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void whenAdminGetsAllProjects_thenSeesAll() throws Exception {
        mockMvc.perform(get("/api/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}
