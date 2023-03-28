package com.zijinge.blogapi.Vo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result { //前后端数据统一协议
    private boolean success; //判断是否成功或失败

    private Integer code; //状态码

    private String msg; //消息

    private Object data;    //存储发给前台的数据

    public static Result success(Object data) {
        return new Result(true,200,"success",data);
    }

    public static Result fail(Integer code,String msg) {
        return new Result(false,code,msg,null);
    }
}