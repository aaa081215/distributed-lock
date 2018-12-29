package com.demon;

/**
 * Created by wangpengpeng on 2018/12/29.
 */
public class Service {
    static void buy(String sku){
        try {
            System.out.println(Thread.currentThread().getName()+"正在抢 "+sku+"的票");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"抢票完毕");
    }
}
