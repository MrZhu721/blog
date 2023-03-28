package com.zijinge.blogapi.dos;

import lombok.Data;

/**
 * 用于文章归档创建的pojo类,用于临时存储，所以不需要持久化
 */
@Data
public class Archives {
    private Integer year;
    private Integer month;
    private long count;
}