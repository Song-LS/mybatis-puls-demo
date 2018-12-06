package com.aj.mybatisplusdemo.controller;

import com.aj.mybatisplusdemo.service.UserService;
import com.aj.mybatisplusdemo.util.ColinParamUtil;
import com.aj.mybatisplusdemo.vo.ResultVo;
import io.swagger.annotations.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author colin
 * @date 2018-12-04
 **/
@RestController
@Api(description = "用户")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/addUser")
    @ApiOperation(value = "添加员工", httpMethod = "POST", response = ApiResponse.class)
    @ApiImplicitParam(name = "name", value = "名字", dataType = "String", required = true, paramType = "Query")
    public ResultVo addUser(String name) {
        if (StringUtils.isEmpty(name)) {
            return ResultVo.error("参数不能为空");
        }
        return userService.addUser(name);
    }

    @PostMapping("/findById")
    @ApiOperation(value = "通过Id查询员工", httpMethod = "POST", response = ApiResponse.class)
    @ApiImplicitParam(name = "id", value = "用户Id", dataType = "String", required = true, paramType = "Query")
    public ResultVo findById(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultVo.error("参数不能为空");
        }
        return userService.findById(Long.valueOf(id));
    }

    @GetMapping("/findUserList")
    @ApiOperation(value = "查询用户列表", httpMethod = "GET", response = ApiResponse.class)
    public ResultVo findUserList() {
        return userService.findUserList();
    }

    @PostMapping("/findPage")
    @ApiOperation(value = "分页查询用户列表", httpMethod = "POST", response = ApiResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "开始", dataType = "String", required = true, paramType = "Query"),
            @ApiImplicitParam(name = "end", value = "结束", dataType = "String", required = true, paramType = "Query")
    })
    public ResultVo findPage(String start, String end) {
        if (ColinParamUtil.paramCheck(Arrays.asList(start, end))) {
            return ResultVo.error("参数不能为空");
        }
        return userService.findPage(Integer.valueOf(start), Integer.valueOf(end));
    }

    @PostMapping("/delUser")
    @ApiOperation(value = "删除用户", httpMethod = "POST", response = ApiResponse.class)
    @ApiImplicitParam(name = "id", value = "用户iD", required = true, dataType = "String", paramType = "Query")
    public ResultVo delUser(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultVo.error("参数不能为空");
        }
        return userService.delUser(Long.valueOf(id));
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "修改用户", httpMethod = "POST", response = ApiResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", dataType = "String", required = true, paramType = "Query"),
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "String", required = true, paramType = "Query")
    })
    public ResultVo updateUser(String id, String name) {
        if (ColinParamUtil.paramCheck(Arrays.asList(id, name))) {
            return ResultVo.error("参数不能为空");
        }
        return userService.updateUser(Long.valueOf(id), name);
    }
}
