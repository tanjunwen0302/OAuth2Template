package uaa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * (Rights)实体类
 *
 * @author makejava
 * @since 2022-05-12 17:19:21
 */
@TableName("rights")
@Data
public class Rights implements Serializable {
    private static final long serialVersionUID = -15861798502721637L;
    /**
    * 权限id
    */
    private Integer rightsId;
    /**
    * 权限
    */
    private String rights;


    public Integer getRightsId() {
        return rightsId;
    }

    public void setRightsId(Integer rightsId) {
        this.rightsId = rightsId;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

}