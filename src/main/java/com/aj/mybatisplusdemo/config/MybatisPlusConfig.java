package com.aj.mybatisplusdemo.config;

import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author colin
 * @date 2018-12-05
 **/
@Configuration
public class MybatisPlusConfig {

    /**
     * 注册逻辑删除bean,使用了逻辑删除需要在实体类的删除字段上加上@TableLogic
     * @return ISqlInjector
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 自动插入字段
     * @return MyMetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }
}
