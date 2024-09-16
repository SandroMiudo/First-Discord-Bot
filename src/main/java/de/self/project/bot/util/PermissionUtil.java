package de.self.project.bot.util;

import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static List<Permission> buildPermissions(List<Integer> offsets){
        return offsets.stream().map(Permission::getFromOffset).toList();
    }

    public static Permission admin = Permission.ADMINISTRATOR;

    // membership permission + text permission + thread permission + voice permission
    public static List<Permission> CO_OWNER_PERMISSION(){
        List<Permission> permissions = new ArrayList<>();
        permissions.addAll(Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS).stream().toList());
        permissions.addAll(Permission.getPermissions(Permission.ALL_TEXT_PERMISSIONS));
        permissions.addAll(List.of(Permission.CREATE_INSTANT_INVITE,Permission.KICK_MEMBERS,
                Permission.BAN_MEMBERS,Permission.NICKNAME_CHANGE,Permission.NICKNAME_MANAGE,
                Permission.MODERATE_MEMBERS,Permission.MANAGE_THREADS,Permission.CREATE_PUBLIC_THREADS,
                Permission.CREATE_PRIVATE_THREADS,Permission.MESSAGE_SEND_IN_THREADS));
        return permissions;
    }

    // text permission + voice permission - voice.move
    public static List<Permission> MEMBER_PERMISSION(){
        List<Permission> permissions = new ArrayList<>();
        permissions.addAll(Permission.getPermissions(Permission.ALL_TEXT_PERMISSIONS).stream().toList());
        List<Permission> voicePermissions = Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS)
                .stream().filter(x -> x.getOffset() != 24).toList();
        permissions.addAll(voicePermissions);
        return permissions;
    }
}
