package com.extjs.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/30.
 */
@Entity
@Table(name = "tb_role_menu")
public class RoleMenuKey implements Serializable {
//    @ManyToOne(
//            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
//            fetch = FetchType.EAGER
//    )
//    @JoinColumn(
//            name = "role_id"
//    )
//    private Role role;
//    @ManyToOne(
//            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
//            fetch = FetchType.EAGER
//    )
//    @JoinColumn(
//            name = "menu_id"
//    )
//    private Menu menu;

    @Id
    @Column(name = "role_id")
    private String roleId;
    @Id
    @Column(name = "menu_id")
    private String menuId;

//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public Menu getMenu() {
//        return menu;
//    }
//
//    public void setMenu(Menu menu) {
//        this.menu = menu;
//    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
