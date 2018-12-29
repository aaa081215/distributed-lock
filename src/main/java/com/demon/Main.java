package com.demon;

import com.demon.RedisLock.Lock;
import redis.clients.jedis.Jedis;

/**
 * Created by wangpengpeng on 2018/12/29.
 */
public class Main {

    public static void main(String[] args) {
        String sku= "北京-济南";
        if(Lock.lock(sku)){
            Service.buy(sku);
            Lock.unLock(sku);
        }
    }
}
