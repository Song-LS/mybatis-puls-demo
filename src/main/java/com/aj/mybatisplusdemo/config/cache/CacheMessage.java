package com.aj.mybatisplusdemo.config.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * @author colin
 * @date 2018-12-06
 **/
@Data
class CacheMessage implements Serializable {

    private String cacheName;

    private Object key;

    CacheMessage(String cacheName, Object key) {
        super();
        this.cacheName = cacheName;
        this.key = key;
    }
}
