package com.extjs.service.impl;

import com.extjs.dao.MenuDao;
import com.extjs.dao.RoleMenuKeyDao;
import com.extjs.model.MenuDTO;
import com.extjs.model.RoleMenuKey;
import com.extjs.util.JSONTreeNode;
import com.extjs.model.Menu;
import com.extjs.service.MenuService;
import com.extjs.util.SysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
@Service
@Scope("prototype")
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleMenuKeyDao roleMenuKeyDao;

    @Override
    public List<JSONTreeNode> getMenuJSONTreeNode() {
//        List<Menu> menus = menuDao.selectParentMenu();
//        System.out.println("parentMenu:"+menus.size());
        JSONTreeNode jsonTreeNode=null;
        List<JSONTreeNode> jsonTreeNodeList=new ArrayList<JSONTreeNode>();
//        for (Menu menu : menus) {
//            jsonTreeNode=new JSONTreeNode();
//            jsonTreeNode.setId(menu.getMenuId());
//            jsonTreeNode.setDescription(menu.getMenuDescription());
//            jsonTreeNode.setText(menu.getMenuTitle());
//            jsonTreeNode.setUrl(menu.getMenuUrl());
//            jsonTreeNode.setChildren(this.getNextMenuJSONTreeNode(menu.getMenuId()));
//            jsonTreeNodeList.add(jsonTreeNode);
//        }
        jsonTreeNode=new JSONTreeNode();
        jsonTreeNode.setId(null);
        jsonTreeNode.setText("所有菜单");
        jsonTreeNode.setChildren(this.getNextMenuJSONTreeNode(null,null));
        jsonTreeNode.setLeaf(false);
        jsonTreeNodeList.add(jsonTreeNode);
        return jsonTreeNodeList;
    }

    @Override
    public List<JSONTreeNode> getNextMenuJSONTreeNode(String menuId,String[] menuIds) {
        List<Menu> menus = menuDao.selectChildrenMenu(menuId,menuIds);
        System.out.println("childrenMenu:"+menus.size());
        List<JSONTreeNode> jsonTreeNodes=new ArrayList<JSONTreeNode>();
        JSONTreeNode jsonTreeNode=null;
            for (Menu menu : menus) {
                jsonTreeNode=new JSONTreeNode();
                jsonTreeNode.setId(menu.getMenuId());
                jsonTreeNode.setDescription(menu.getMenuDescription());
                jsonTreeNode.setText(menu.getMenuTitle());
                jsonTreeNode.setUrl(menu.getMenuUrl());
                List<JSONTreeNode> nextMenuJSONTreeNode = this.getNextMenuJSONTreeNode(menu.getMenuId(),menuIds);
                if (nextMenuJSONTreeNode!=null&&nextMenuJSONTreeNode.size()>0){
                    jsonTreeNode.setChildren(nextMenuJSONTreeNode);
                    jsonTreeNode.setLeaf(false);
                }else {
                    jsonTreeNode.setLeaf(true);
                }
                jsonTreeNodes.add(jsonTreeNode);
            }

        return jsonTreeNodes;
    }

    @Override
    public void addMenu(MenuDTO menuDTO) throws SysException {
        if (menuDTO.getParentMenuId()==null||"".equals(menuDTO.getParentMenuId())){
            menuDTO.setParentMenuId(null);
        }
        menuDao.addMenu(menuDTO);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class},propagation = Propagation.REQUIRED)
    public void delMenu(String menuId) throws SysException {
        List<Menu> menus = menuDao.selectChildrenMenu(menuId,null);
        for (Menu menu : menus) {
            this.delMenu(menu.getMenuId());
        }
        menuDao.delMenu(menuId);
        roleMenuKeyDao.deleteRoleMenuKey(menuId);
    }

    @Override
    public List<JSONTreeNode> getMenuByUserRole(String[]roleIds) {
        List<RoleMenuKey> roleMenuKeys = roleMenuKeyDao.getRoleMenuKeys(roleIds);
        List<String> menuIds=new ArrayList<String>();
        for (RoleMenuKey roleMenuKey : roleMenuKeys) {
            if (!menuIds.contains(roleMenuKey.getMenuId())){
                menuIds.add(roleMenuKey.getMenuId());
            }
        }
        String[] ids = menuIds.toArray(new String[menuIds.size()]);
        List<Menu> menus = menuDao.selectMenus(ids);
        List<JSONTreeNode> jsonTreeNodes=new ArrayList<JSONTreeNode>();
        JSONTreeNode jsonTreeNode=null;
        for (Menu menu : menus) {
            jsonTreeNode=new JSONTreeNode();
            jsonTreeNode.setId(menu.getMenuId());
            jsonTreeNode.setDescription(menu.getMenuDescription());
            jsonTreeNode.setText(menu.getMenuTitle());
            jsonTreeNode.setUrl(menu.getMenuUrl());
            List<JSONTreeNode> nextMenuJSONTreeNode = this.getNextMenuJSONTreeNode(menu.getMenuId(),ids);
            if (nextMenuJSONTreeNode!=null&&nextMenuJSONTreeNode.size()>0){
                jsonTreeNode.setChildren(nextMenuJSONTreeNode);
                jsonTreeNode.setLeaf(false);
            }else {
                jsonTreeNode.setLeaf(true);
            }
            jsonTreeNodes.add(jsonTreeNode);
        }

        return jsonTreeNodes;
    }


}
