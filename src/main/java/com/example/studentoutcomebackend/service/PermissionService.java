package com.example.studentoutcomebackend.service;

public interface PermissionService {

    boolean checkPermission(int userId, String permissionName);

    /**
     * 检查当前登录的用户有没有某个权限
     *
     * @param permissionName 权限节点名
     * @return 有没有权限
     */
    boolean checkPermission(String permissionName);

    /**
     * 检查userId的用户有没有某个权限，没有就直接抛异常
     *
     * @param permissionName 权限节点名
     * @param hint           如果不是null就把提示换成这个，否则构造默认提示
     */
    void throwIfDontHave(int userId, String permissionName, String hint);

    /**
     * 推荐！检查登录的用户有没有某个权限，没有就直接抛异常
     *
     * @param permissionName 权限节点名
     * @param hint           如果不是null就把提示换成这个，否则构造默认提示
     */
    void throwIfDontHave(String permissionName, String hint);

}
