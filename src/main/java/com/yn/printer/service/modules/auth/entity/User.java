package com.yn.printer.service.modules.auth.entity;

import com.google.common.base.MoreObjects;
import com.yn.printer.service.common.entity.AuditableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户实体
 * 此实体类只展示一部分核心包字段数据，有需要可自行添加
 *
 * @author huabiao
 * @create 2021/8/31  17:26
 */
@Entity
@Cacheable
@Table(name = "AUTH_USER")
public class User extends AuditableModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_USER_SEQ")
    @SequenceGenerator(name = "AUTH_USER_SEQ", sequenceName = "AUTH_USER_SEQ", allocationSize = 1)
    private Long id;

    /**
     * 登陆名
     */
    @Column(unique = true)
    private String code;

    /**
     * 昵称
     */
    private String name;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 上次密码更新日期
     */
    private LocalDateTime passwordUpdatedOn;

    /**
     * 是否忘记密码
     */
    private Boolean forcePasswordChange = Boolean.FALSE;

    /**
     * 头像
     */
    private byte[] image;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
  //  private String phone;

    public User() {
    }

    public User(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getPasswordUpdatedOn() {
        return passwordUpdatedOn;
    }

    public void setPasswordUpdatedOn(LocalDateTime passwordUpdatedOn) {
        this.passwordUpdatedOn = passwordUpdatedOn;
    }

    /**
     * Force the user to change their password at next login.
     *
     * @return the property value
     */
    public Boolean getForcePasswordChange() {
        return forcePasswordChange == null ? Boolean.FALSE : forcePasswordChange;
    }

    public void setForcePasswordChange(Boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    /**
     * Max size 4MB.
     *
     * @return the property value
     */
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPhone() {
//        return phone;
//    }

  /*
    public void setPhone(String phone) {
        this.phone = phone;
    }
  */

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        final User other = (User) obj;
        if (this.getId() != null || other.getId() != null) {
            return Objects.equals(this.getId(), other.getId());
        }

        if (!Objects.equals(getCode(), other.getCode())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(2645995, this.getCode());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("code", getCode())
                .add("name", getName())
                .add("passwordUpdatedOn", getPasswordUpdatedOn())
                .add("forcePasswordChange", getForcePasswordChange())
                .add("email", getEmail())
              //  .add("phone", getPhone())
                .omitNullValues()
                .toString();
    }
}