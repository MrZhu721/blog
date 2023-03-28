package com.zijinge.blogapi.utils;

import com.zijinge.blogapi.pojo.SysUser;


public class UserThreadLocal {

    private UserThreadLocal() {}

    // 线程变量隔离
    // 利用 Thread Local 采用线程隔离 可以防止获取多用户时数据之间的污染
    // 使用ThreadLocal时，一般建议将其声明为static final的，避免频繁创建ThreadLocal实例
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static  void remove() {
        LOCAL.remove();
    }
}

/**
 * 内存泄露：
 * 当 new 一个 ThreadLocal（线程变量） 后，每一个 Thread 维护一个 ThreadLocalMap，而 key 是ThreadLocal 的实例，value是线程变量的副本
 * 强引用（不会被垃圾回收站回收）： 当程序运行时，ThreadLocal 在线程的某个时间点停止 ThreadLocal 连带着它的实例不再维护ThreadLocalMap 但value副本没有销毁
 * 又由于map之间没有key的联系 便找不到这个value，但value会一直占用内存，导致内存溢出，程序崩溃
 */