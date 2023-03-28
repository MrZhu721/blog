package com.zijinge.blogapi.utils;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 有三部分组成：A.B.C
 * A：Header，{"type":"JWT","alg":"HS256"} 固定
 * B：playload，存放信息，比如，用户id，过期时间等等，可以被解密，不能存放敏感信息
 * C:  签证，A和B加上秘钥 加密而成，只要秘钥不丢失，可以认为是安全的。
 * jwt 验证，主要就是验证C部分 是否合法。
 */
public class JWTUtils {
    private static final  String jwtToken = "123456zijinge@zzh"; //设置C部分密钥

    //  创建token令牌
    public static  String createToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);    // 设置B部分
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken) //签发算法,密钥为jwtToken
                .setClaims(claims)   //  body数据,要唯一
                .setIssuedAt(new Date())    // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*60*1000));    // 设置token令牌有效期(过期时间)
        String token = jwtBuilder.compact();    // 生成 token对象
        return token;
    }

    //  解析token令牌是否合法
    public static  Map<String,Object> checkToken(String token) {
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}