package com.taskpro.backend.enums;

import java.util.EnumSet;

public enum CollaboratorRoleEnum {
    OWNER,
    EDITOR,
    VIEWER;

    public boolean hasReadPermission() {
        return EnumSet.of(OWNER, EDITOR, VIEWER).contains(this);
    }

    public boolean hasEditPermission() {
        return EnumSet.of(OWNER, EDITOR).contains(this);
    }

    public boolean hasOwnership() {
        return EnumSet.of(OWNER).contains(this);
    }
}
