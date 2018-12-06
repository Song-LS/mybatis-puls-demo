package com.aj.mybatisplusdemo.config;
import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 注入公共字段，这个类不要改动
 * @author colin
 */
public class MyMetaObjectHandler extends MetaObjectHandler {

    /**
     * 无论之前有没有值，在新增时设置对应字段 会覆盖
     */
    @Override
    public void insertFill(MetaObject metaObject) {
    	setFieldValByName("deleted", 0, metaObject);
    	setFieldValByName("createTime", new Date(), metaObject);
    	setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    	setFieldValByName("updateTime", new Date(), metaObject);
    }

    public void insertUpdateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
