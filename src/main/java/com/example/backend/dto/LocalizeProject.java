/* example.backend.dto.LocalizeProject.java */
package com.example.backend.dto;
import java.time.LocalDate;
import java.util.Locale;
import org.springframework.context.MessageSource;
import com.example.backend.entity.Project;
import lombok.Data;

@Data
public class LocalizeProject {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String localizedStatus;

    private boolean visible;
    private LocalDate startDate;
    private LocalDate endDate;

    public LocalizeProject (Project project, MessageSource messageSource, Locale locale) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.status = project.getStatus() != null ? project.getStatus().name() : null;
        this.localizedStatus = project.getStatus().getLocalized(messageSource, locale);
        this.visible = project.isVisible();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
    }
}
