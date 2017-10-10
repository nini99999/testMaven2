package com.extjs.service.impl;

import com.extjs.dao.UserDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/2/19.
 */
@Service
@Scope("prototype")
@Transactional
public class CustomUserDetailsService implements UserDetailsService{
    private static Log logger= LogFactory.getLog(CustomUserDetailsService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageSource messageSource;

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails user=null;
//        try {
            List<com.extjs.model.User> userList = userDao.getUserList(userName);
            if (userList!=null&&userList.size()>0){
                com.extjs.model.User user1 = userList.get(0);
                user=new User(user1.getUserName(),user1.getUserPassword(),true,true,true,true,getAuthorities(0));
            }else {
                logger.debug("用户不存在!");
                throw new UsernameNotFoundException(this.messageSource.getMessage("UserDetailsService.userNotFount",new  Object[]{userName},null));
            }
//        } catch (Exception e) {
//            e.printStackTrace();
////            throw new UsernameNotFoundException("用户不存在!");
//        }
        return user;
    }
    /**
     * 获得访问角色权限
     *
     * @param access
     * @return
     */
    public Collection<GrantedAuthority> getAuthorities(Integer access) {

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);

        // 所有的用户默认拥有ROLE_USER权限
//        logger.debug("Grant ROLE_USER to this user");
        System.out.println("Grant ROLE_USER to this user");
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        // 如果参数access为1.则拥有ROLE_ADMIN权限
        if (access.compareTo(1) == 0) {
//            logger.debug("Grant ROLE_ADMIN to this user");
            authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("Grant ROLE_ADMIN to this user");
        }

        return authList;
    }
}
