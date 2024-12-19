package edu.pradita.p14.permission;

import java.util.*;

public class PermissionManager {
    private Map<String, List<String>> permissions;

    private static PermissionManager INSTANCE;
    
    
    private PermissionManager() {
        permissions = new HashMap<>();
    }

    public static PermissionManager getInstance() {
        if(INSTANCE == null)
            INSTANCE = new PermissionManager();
        return INSTANCE;
    }
    
    public Map<String, List<String>> getPermissions() {
        return permissions;
    }
    public void setPermissions(Map<String, List<String>> permissions) {
        this.permissions = permissions;
    }

    public void addPermissions(Map<String, List<String> permissions) {
        this.permissions.putAll(permissions);
    }

    public boolean hasPermission(String role, String permission) {
        List<String> permissions = this.permissions.get(role);
        if(permissions == null) return false;
        return permissions.contains(permission);
    }

    public void addPermission(String role, String permission) {
        List<String> permissions = this.permissions.get(role);
        if(permissions == null) {
            permissions = new ArrayList<>();
            this.permissions.put(role, permissions);
        }
        permissions.add(permission);
    }
    public void removePermission(String role, String permission) {
        List<String> permissions = this.permissions.get(role);
        if(permissions != null) {
            permissions.remove(permission);
        }
    }

    public boolean hasPermission(IPermissible permissible, String permission) {
        return hasPermission(permissible.getRole(), permission);
    }
    public void addPermission(IPermissible permissible, String permission) {
        addPermission(permissible.getRole(), permission);
    }
    public void removePermission(IPermissible permissible, String permission) {
        removePermission(permissible.getRole(), permission);
    }

}
