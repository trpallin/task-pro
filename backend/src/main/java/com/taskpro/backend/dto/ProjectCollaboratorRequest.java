package com.taskpro.backend.dto;

import com.taskpro.backend.enums.CollaboratorRoleEnum;
import lombok.Data;

@Data
public class ProjectCollaboratorRequest {
    private String userEmail;
    private CollaboratorRoleEnum role;
}
