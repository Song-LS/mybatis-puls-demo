package com.aj.mybatisplusdemo.dao;

import com.aj.mybatisplusdemo.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * @author colin
 * @date 2018-12-04
 **/
public interface UserDao extends BaseMapper<User> {
    /**
     * 查找用户列表
     * @return 用户列表
     */
    List<User> findUserList();
}
