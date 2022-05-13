package uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import uaa.entity.Rights;

import java.util.List;

/**
 * (Rights)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-12 17:19:21
 */
public interface RightsDao extends BaseMapper<Rights> {

    /**
     * 通过ID查询单条数据
     *
     * @param rightsId 主键
     * @return 实例对象
     */
    Rights queryById(Integer rightsId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Rights> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param rights 实例对象
     * @return 对象列表
     */
    List<Rights> queryAll(Rights rights);

    /**
     * 新增数据
     *
     * @param rights 实例对象
     * @return 影响行数
     */
    int insert(Rights rights);

    /**
     * 修改数据
     *
     * @param rights 实例对象
     * @return 影响行数
     */
    int update(Rights rights);

    /**
     * 通过主键删除数据
     *
     * @param rightsId 主键
     * @return 影响行数
     */
    int deleteById(Integer rightsId);


    /**
     * 根据用户id查询权限
     * */
    List<String> queryPermission(String id);

}