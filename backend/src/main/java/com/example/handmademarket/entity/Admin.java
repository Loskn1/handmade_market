package com.example.handmademarket.entity;

<<<<<<< HEAD
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
import java.time.LocalDateTime;
>>>>>>> my111

@Entity
@Table(name = "tb_admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
<<<<<<< HEAD
    private Long adminId;  // 管理员ID，自增

    @Column(name = "admin_account", unique = true, nullable = false)
    private String adminAccount;  // 管理员账号，唯一

    @Column(name = "admin_pwd", nullable = false)
    private String adminPwd;  // 管理员密码，加密存储

    @Column(name = "admin_name")
    private String adminName;  // 管理员真实姓名

    @Column(name = "permission_level")
    private Integer permissionLevel;  // 1-超级管理员 2-普通管理员

    @Column(name = "create_time")
    private Date createTime;  // 账号创建时间

    @Column(name = "update_time")
    private Date updateTime;  // 更新时间

    @Column(name = "status", columnDefinition = "INT DEFAULT 1")
    private Integer status;  // 0-禁用 1-正常

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(Integer permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
=======
    private Integer adminId;

    @Column(name = "admin_account", unique = true, nullable = false, length = 50)
    private String adminAccount;

    @Column(name = "admin_pwd", nullable = false, length = 100)
    private String adminPwd;

    @Column(name = "admin_name", length = 20)
    private String adminName;

    @Column(name = "permission_level")
    private Integer permissionLevel;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "status")
    private Integer status;

    // Getters and Setters

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public String getAdminAccount() { return adminAccount; }
    public void setAdminAccount(String adminAccount) { this.adminAccount = adminAccount; }

    public String getAdminPwd() { return adminPwd; }
    public void setAdminPwd(String adminPwd) { this.adminPwd = adminPwd; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public Integer getPermissionLevel() { return permissionLevel; }
    public void setPermissionLevel(Integer permissionLevel) { this.permissionLevel = permissionLevel; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
>>>>>>> my111
