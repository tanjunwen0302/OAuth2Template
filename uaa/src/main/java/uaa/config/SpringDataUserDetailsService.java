package uaa.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uaa.dao.RightsDao;
import uaa.dao.UserDao;
import uaa.entity.User;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tanjunwen
 * @version 1.0
 * @Purpose 从数据库中查询用户数据和对应的权限数据写入UserDetails中
 * @Date 2022/5/12
 * */

@Service
public class SpringDataUserDetailsService implements UserDetailsService {
    @Resource
    UserDao userDao;
    @Resource
    RightsDao rightsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.queryByName(username);

        if (null == user) {
            return null;
        }
        //根据用户id查询权限
        List<String> permissions = rightsDao.queryPermission(user.getId());
        //将permissions转为数组
        String[] permissionArrays = new String[permissions.size()];

        permissions.toArray(permissionArrays);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(permissionArrays)
                .build();

        return userDetails;
    }
}
