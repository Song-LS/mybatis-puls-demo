package com.aj.mybatisplusdemo.service;

import com.aj.mybatisplusdemo.vo.ResultVo;

/**
 * @author colin
 * @date 2018-12-05
 **/
public interface UserService {
    /**
     * 添加用户
     * @param name 用户名
     * @return resultVo
     */
    ResultVo addUser(String name);

    /**
     * 通过ID查询
     * @param id 用户Id
     * @return resultVo
     */
    ResultVo findById(Long id);

    /**
     * 查询用户列表
     * @return resultVo
     */
    ResultVo findUserList();

    /**
     * 分页查询用户列表
     * @param start 页数
     * @param end  一页几条
     * @return resultVo
     */
    ResultVo findPage(Integer start,Integer end);

    /**
     * 删除用户
     * @param id 用户Id
     * @return resultVo
     */
    ResultVo delUser(Long id);

    /**
     * 修改用户
     * @param id 用户Id
     * @param name 用户名
     * @return resultVo
     */
    ResultVo updateUser(Long id, String name);

}
