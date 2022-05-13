package uaa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2022-05-12 17:18:05
 */
@TableName("user")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -23662855331399125L;
    /**
    * 用户id
    */
    private String id;
    /**
    * 用户登录名
    */
    private String username;
    /**
    * 密码
    */
    private String password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}