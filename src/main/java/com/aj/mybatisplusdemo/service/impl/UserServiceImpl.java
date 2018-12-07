package com.aj.mybatisplusdemo.service.impl;

import com.aj.mybatisplusdemo.dao.UserDao;
import com.aj.mybatisplusdemo.entity.User;
import com.aj.mybatisplusdemo.service.UserService;
import com.aj.mybatisplusdemo.vo.ResultVo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author colin
 * @date 2018-12-05
 **/
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public ResultVo addUser(String name) {
        User user = new User();
        user.setName(name);
        int index = userDao.insert(user);
        if (index > 0) {
            return ResultVo.success("", "", null);
        }
        return ResultVo.error("");
    }

    @Override
    @Cacheable(key = "#id",value = "user")
    public ResultVo findById(Long id) {
        User user = userDao.selectById(id);
        if (user == null) {
            return ResultVo.error("用户不存在");
        }
        return ResultVo.success("", user, null);
    }

    @Override
    @Cacheable(value = "userList")
    public ResultVo findUserList() {
        List<User> userList = userDao.selectList(null);
        if (userList.isEmpty()) {
            return ResultVo.error("数据不存在");
        }
        return ResultVo.success("", userList, null);
    }

    @Override
    public ResultVo findPage(Integer start,Integer end) {
        List<User> userPage = userDao.selectPage(new Page<User>(start, end), new EntityWrapper<>(new User()).orderDesc(Collections.singletonList("id")));
        List<Map<String, Object>> data = new LinkedList<>();
        userPage.forEach(e->{
            Map<String, Object> map = new HashMap<>(2);
            map.put("id", e.getId());
            map.put("name", e.getName());
            data.add(map);
        });
        int size = userDao.selectList(null).size();
        return ResultVo.success("", data, size);
    }

    @Override
    @CacheEvict(cacheNames = "user",key = "#id")
    public ResultVo delUser(Long id) {
        int index = userDao.deleteById(id);
        if (index > 0) {
            return ResultVo.success("", "", null);
        }
        return ResultVo.error("删除失败,用户不存在");
    }

    @Override
    @CacheEvict(value = "user",key = "#id")
    public ResultVo updateUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        int index = userDao.updateById(user);
        if (index > 0) {
            return ResultVo.success("", "", null);
        }
        return ResultVo.error("更新失败");
    }

    @Override
    @Async
    public void sys() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i+"线程:"+Thread.currentThread().getName());
        }
    }


}
