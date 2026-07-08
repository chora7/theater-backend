/* java.com.example.backend.controller.PageController.java */
package com.example.backend.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.example.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.backend.entity.Project;
import com.example.backend.dto.LocalizeProject;
import org.springframework.context.MessageSource;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.backend.repository.UserRepository;
import com.example.backend.entity.Department;
import com.example.backend.service.DepartmentService;
import com.example.backend.repository.LocationRepository;

import com.example.backend.entity.Performance;
import com.example.backend.entity.PerformanceId;
import com.example.backend.dto.PerformanceUpdateRequest;
import com.example.backend.service.PerformanceService;

@Controller
public class PageController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PerformanceService performanceService;
    
    @GetMapping("/login")
    public String loginPage () {
        return "auth/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "admin/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "user/dashboard";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @GetMapping("/admin/projects/list")
    public String listProjects(Model model, Locale locale) {
        List<Project> projects = projectService.getAllForCurrentUser();
        List<LocalizeProject> localizedProjects = projects.stream()
                .map(p -> new LocalizeProject(p, messageSource, locale))
                .collect(Collectors.toList());
        model.addAttribute("projects", localizedProjects);
        model.addAttribute("allUsers", userRepository.findAll()); 
        return "admin/projects/list";
    }

    @GetMapping("/admin/projects/new")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "admin/projects/form";
    }

    @PostMapping("/admin/projects/new")
    public String processCreateProject(@ModelAttribute Project project, Model model) {
        if (project.getTitle() == null || project.getTitle().isBlank()) {
            model.addAttribute("error", "Title is required.");
            model.addAttribute("project", project);
            return "admin/projects/form";
        }

        if (project.getStartDate() != null && project.getEndDate() != null
                && project.getStartDate().isAfter(project.getEndDate())) {
            model.addAttribute("error", "Start date must be before end date.");
            model.addAttribute("project", project);
            return "admin/projects/form";
        }

        try {
            projectService.create(project);
            return "redirect:/admin/projects/list?created";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create project: " + e.getMessage());
            model.addAttribute("project", project);
            return "admin/projects/form";
        }
    }

    @GetMapping("/admin/projects/{id}/edit")
    public String showEditProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.getByIdForCurrentUser(id);
        model.addAttribute("project", project);
        return "admin/projects/form";
    }

    @PostMapping("/admin/projects/{id}/edit")
    public String processEditProject(@PathVariable Long id,
                                      @ModelAttribute Project project,
                                      Model model) {
        if (project.getTitle() == null || project.getTitle().isBlank()) {
            model.addAttribute("error", "Title is required.");
            return "admin/projects/form";
        }

        if (project.getStartDate() != null && project.getEndDate() != null
                && project.getStartDate().isAfter(project.getEndDate())) {
            model.addAttribute("error", "Start date must be before end date.");
            model.addAttribute("project", project);
            return "admin/projects/form";
        }

        try {
            projectService.update(id, project);
            return "redirect:/admin/projects/list?updated";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update project: " + e.getMessage());
            return "admin/projects/form";
        }
    }

    @PostMapping("/admin/projects/{id}/delete")
    public String processDeleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/admin/projects/list?deleted";
    }

    @PostMapping("/admin/projects/{projectId}/assign")
    public String assignUserToProject(@PathVariable Long projectId,
                                       @RequestParam Long userId) {
        projectService.assignProjectToUser(projectId, userId);
        return "redirect:/admin/projects/list?assigned";
    }

    @GetMapping("/user/projects")
    public String userProjects(Model model, Locale locale) {
        List<Project> projects = projectService.getAllForCurrentUser();
        List<LocalizeProject> localizedProjects = projects.stream()
                .map(p -> new LocalizeProject(p, messageSource, locale))
                .collect(Collectors.toList());
        model.addAttribute("projects", localizedProjects);
        return "user/projects";
    }

    @GetMapping("/admin/departments/list")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllForCurrentUser());
        model.addAttribute("allUsers", userRepository.findAll());
        return "admin/departments/list";
    }

    @GetMapping("/admin/departments/new")
    public String showCreateDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "admin/departments/form";
    }

    @PostMapping("/admin/departments/new")
    public String processCreateDepartment(@ModelAttribute Department department, Model model) {
        if (department.getName() == null || department.getName().isBlank()) {
            model.addAttribute("error", "Name is required.");
            return "admin/departments/form";
        }
        try {
            departmentService.create(department);
            return "redirect:/admin/departments/list?created";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create department: " + e.getMessage());
            return "admin/departments/form";
        }
    }

    @GetMapping("/admin/departments/{id}/edit")
    public String showEditDepartmentForm(@PathVariable Long id, Model model) {
        model.addAttribute("department", departmentService.getById(id));
        return "admin/departments/form";
    }

    @PostMapping("/admin/departments/{id}/edit")
    public String processEditDepartment(@PathVariable Long id,
                                         @ModelAttribute Department department,
                                         Model model) {
        if (department.getName() == null || department.getName().isBlank()) {
            model.addAttribute("error", "Name is required.");
            return "admin/departments/form";
        }
        try {
            departmentService.update(id, department);
            return "redirect:/admin/departments/list?updated";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update department: " + e.getMessage());
            return "admin/departments/form";
        }
    }

    @PostMapping("/admin/departments/{id}/delete")
    public String processDeleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return "redirect:/admin/departments/list?deleted";
    }

    @PostMapping("/admin/departments/{id}/assign")
    public String assignUserToDepartment(@PathVariable Long id, @RequestParam Long userId) {
        departmentService.assignDepartmentToUser(id, userId);
        return "redirect:/admin/departments/list?assigned";
    }

    @GetMapping("/admin/locations/list")
    public String listLocations(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        return "admin/locations/list";
    }

    @GetMapping("/admin/performances/list")
    public String listPerformances(Model model) {
        model.addAttribute("performances", performanceService.getAllForCurrentUser());
        return "admin/performances/list";
    }

    @GetMapping("/admin/performances/{userId}/{projectId}/edit")
    public String showEditPerformanceForm(@PathVariable Long userId,
                                           @PathVariable Long projectId,
                                           Model model) {
        Performance performance = performanceService.getPerformanceById(new PerformanceId(userId, projectId));

        PerformanceUpdateRequest updateRequest = new PerformanceUpdateRequest();
        updateRequest.setRole(performance.getRole());
        updateRequest.setSpecialization(performance.getSpecialization());
        updateRequest.setStatus(performance.getStatus());

        model.addAttribute("performance", performance);
        model.addAttribute("updateRequest", updateRequest);
        return "admin/performances/form";
    }

    @PostMapping("/admin/performances/{userId}/{projectId}/edit")
    public String processEditPerformance(@PathVariable Long userId,
                                          @PathVariable Long projectId,
                                          @ModelAttribute PerformanceUpdateRequest updateRequest,
                                          Model model) {
        if (updateRequest.getRole() == null || updateRequest.getRole().isBlank()) {
            model.addAttribute("error", "Role is required.");
            Performance performance = performanceService.getPerformanceById(new PerformanceId(userId, projectId));
            model.addAttribute("performance", performance);
            model.addAttribute("updateRequest", updateRequest);
            return "admin/performances/form";
        }
        performanceService.update(new PerformanceId(userId, projectId), updateRequest);
        return "redirect:/admin/performances/list?updated";
    }

    @GetMapping("/user/performances")
    public String userPerformances(Model model) {
        model.addAttribute("performances", performanceService.getAllForCurrentUser());
        return "user/performances";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute User user,
            @RequestParam(defaultValue = "false") boolean isAdmin,
            Model model) {

        // Server-side password validation (never trust client-side only)
        String password = user.getPassword();
        if (password == null || password.length() < 8
                || password.chars().allMatch(Character::isDigit)) {
            model.addAttribute("error",
                "Password must be at least 8 characters and not be entirely numeric.");
            return "auth/register";
        }

        try {
            userService.register(user, isAdmin);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }
}
