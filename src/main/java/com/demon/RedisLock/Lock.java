package com.demon.RedisLock;

import redis.clients.jedis.Jedis;

/**
 * Created by wangpengpeng on 2018/12/29.
 */
public class Lock {
    static Jedis redis = new Jedis("106.14.13.61",6379);

    //单位秒 redis锁超时时间，最大容忍业务执行时间 区间
    static Integer expireTime = 50;

    //单位毫秒
    static Long exTime = Long.valueOf(expireTime*1000);

    public static boolean lock(String lockKey){
        boolean success = false;
        Long setNxResult= redis.setnx(lockKey,String.valueOf(System.currentTimeMillis())+exTime);
        //!!!!!!!!!!!!!!!!!问题1，比如执行到此处突然断电
        if(setNxResult!=null&& setNxResult.intValue()==1){
            success =true;
            System.out.println(Thread.currentThread().getName()+"获取锁成功");
            redis.expire(lockKey,expireTime);
        }else {
            //!!!!!!!!!!!!!解决问题1
            String lockValue = redis.get(lockKey);
            if(lockValue!=null && System.currentTimeMillis()>Long.parseLong(lockValue)){
              String newResult = redis.getSet(lockKey,String.valueOf(System.currentTimeMillis())+exTime);
              //getset返回旧值，如果是空，说明被其他线程释放掉，可以获取锁。
              //如果不为空，并且是原值说明没有被其他线程所用，也可以获取锁
              if(newResult == null || (newResult!=null&&newResult.equals(lockValue))){
                  success =true;
                  System.out.println(Thread.currentThread().getName()+"获取锁成功");
              }else {
                  System.out.println(Thread.currentThread().getName()+"获取锁失败");
              }
            }
            System.out.println(Thread.currentThread().getName()+"获取锁失败");
        }
        return success;
    }
    public static void unLock(String lockKey){
            System.out.println(Thread.currentThread().getName()+"删除锁");
            redis.del(lockKey);
    }
}
