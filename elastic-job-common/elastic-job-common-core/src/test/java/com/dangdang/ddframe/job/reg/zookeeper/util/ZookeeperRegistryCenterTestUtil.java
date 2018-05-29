package com.dangdang.ddframe.job.reg.zookeeper.util;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class ZookeeperRegistryCenterTestUtil {

    private ZookeeperRegistryCenterTestUtil() {
    }
    
    public static void persist(final ZookeeperRegistryCenter zookeeperRegistryCenter) {
        zookeeperRegistryCenter.persist("/test", "test");
        zookeeperRegistryCenter.persist("/test/deep/nested", "deepNested");
        zookeeperRegistryCenter.persist("/test/child", "child");
    } 
}
