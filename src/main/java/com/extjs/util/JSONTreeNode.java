package com.extjs.util;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class JSONTreeNode {
    private String id;
    private String text;
    private Boolean leaf;
    private String url;
    private List<JSONTreeNode> children;
    private String description;//描述
    private String menuType;//菜单类型
    private Boolean expanded=true;
    private Boolean checked;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Boolean getLeaf() {
        return leaf;
    }
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<JSONTreeNode> getChildren() {
        return children;
    }
    public void setChildren(List<JSONTreeNode> children) {
        this.children = children;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMenuType() {
        return menuType;
    }
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    public Boolean getChecked() {
        return checked;
    }
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public static void main(String[] args) {
        System.out.println("".split(","));
    }
}
