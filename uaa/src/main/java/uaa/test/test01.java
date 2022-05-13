package uaa.test;

import org.apache.catalina.LifecycleState;
import org.apache.tomcat.util.net.openssl.OpenSSLUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uaa.dao.RightsDao;
import uaa.dao.UserDao;
import uaa.entity.Rights;
import uaa.entity.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class test01 {

    @Resource
    UserDao userDao;

    @Resource
    RightsDao rightsDao;

    @RequestMapping("/test01")
    public void test01() {
        User user = new User();
        user.setUsername("tanjunwen");
        user.setPassword("12345");
        user.setId("1");

        System.out.println(userDao.insert(user));

    }
    @RequestMapping("/test02")
    public void test02(){
//        List<String> list=rightsDao.queryPermission("1");
//        System.out.println(list);

        List<String> list=rightsDao.queryPermission("1");
        System.out.println(list);
    }
}
