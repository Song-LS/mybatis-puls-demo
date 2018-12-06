package com.aj.mybatisplusdemo.vo;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author colin
 * @date 2018-12-04
 **/
@Data
public class ResultVo<T> implements Serializable {
    private String code;
    private String msg;
    private T data;
    private Integer size;

    /**
     * 成功通用返回
     *
     * @param msg  返回信息 使用默认的传null或者""
     * @param data 返回数据 data不需要可以传null或者""
     * @param size 页数 不需要传null
     * @return resultVo
     */
    @SuppressWarnings("unchecked")
    public static ResultVo success(String msg, Object data, Integer size) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode("SUCCESS");
        if (StringUtils.isEmpty(msg)) {
            resultVo.setMsg("操作成功");
        } else {
            resultVo.setMsg(msg);
        }
        if (!StringUtils.isEmpty(data)) {
            resultVo.setData(data);
        } else {
            resultVo.setData("");
        }
        if (!StringUtils.isEmpty(size)) {
            resultVo.setSize(size);
        } else {
            resultVo.setSize(0);
        }
        return resultVo;
    }

    /**
     * 错误返回
     *
     * @param msg 返回信息 使用默认的传null或者""
     * @return resultVo
     */
    public static ResultVo error(String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode("ERROR");
        if (StringUtils.isEmpty(msg)) {
            resultVo.setMsg("操作失败");
        } else {
            resultVo.setMsg(msg);
        }
        return resultVo;
    }

    /**
     * 自定义返回多参数
     *
     * @param keys 参数key
     * @param val  参数value值
     * @return map
     */
    public static Map<String, Object> find(List<String> keys, List<Object> val) {
        Map<String, Object> map = new HashMap<>(16);
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), val.get(i));
        }
        return map;
    }

    /**
     * 登录
     *
     * @param keys   参数key集合,不需要则传null
     * @param values 参数值集合,不需要传null,
     * @return map
     * @implNote keys和values值下标对应
     */
    public static Map<String, Object> login(Integer code, String msg, List<String> keys, List<Object> values) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("code", code);
        map.put("msg", msg);
        if (keys != null && values != null) {
            for (int i = 0; i < keys.size(); i++) {
                map.put(keys.get(i), values.get(i));
            }
        }
        return map;
    }

    /**
     * 错误通用返回结果集
     *
     * @param msg 返回信息
     * @return map
     */
    public static Map<String, Object> isFail(String msg) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("code", "ERROR");
        if (StringUtils.isEmpty(msg)) {
            map.put("msg", "操作失败");
        } else {
            map.put("msg", msg);
        }
        return map;
    }

}
