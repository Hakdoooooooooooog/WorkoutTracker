package com.workouttracker.main.config;

public class PermissionManager {
    public final static int PERM_USER = 1 << 0;
    public final static int PERM_ADMIN = 1 << 1;
    public final static int PERM_COACH = 1 << 2;
    public final static int PERM_GUEST = 1 << 3;

    public static int grantPermission(int currentPermissions, int permissionToGrant) {
        return currentPermissions | permissionToGrant;
    }

    public static int revokePermission(int currentPermissions, int permissionToRevoke) {
        return currentPermissions & ~permissionToRevoke;
    }

    public static boolean hasPermission(int currentPermissions, int permissionToCheck) {
        return (currentPermissions & permissionToCheck) != 0;
    }
}
