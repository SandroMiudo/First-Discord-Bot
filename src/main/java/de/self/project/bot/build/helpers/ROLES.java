package de.self.project.bot.build.helpers;

public enum ROLES {

    MEMBER(1000),OWNER(1005),CO_OWNER(1004),NO_ROLE(1001);

    private Integer permissionBit;

    ROLES(Integer permissionBit) {
        this.permissionBit = permissionBit;
    }

    public Integer getPermissionBitRaw(){
        return permissionBit;
    }
}
