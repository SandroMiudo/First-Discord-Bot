package de.self.project.bot.build.helpers;

public class Role {

    private String role_Name;

    public Role(String role_Name) {
        this.role_Name = role_Name;
    }

    private Role(ROLES roles){
        role_Name = roles.name();
    }

    public static Role buildFromRoles(ROLES roles){
        return new Role(roles);
    }

    public String getRole_Name() {
        return role_Name;
    }
}
