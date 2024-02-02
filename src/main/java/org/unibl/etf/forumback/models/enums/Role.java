package org.unibl.etf.forumback.models.enums;

public enum Role {
    ADMIN("Admin"),
    CLIENT("Client"),
    MODERATOR("Moderator");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString()
    {
        return this.role;
    }

    public static Role getByRole(String role)
    {
        for(var el : Role.values())
            if(el.role.equals(role))
                return el;
        throw new IllegalArgumentException();
    }
}
