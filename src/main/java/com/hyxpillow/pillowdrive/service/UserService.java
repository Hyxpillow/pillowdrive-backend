package com.hyxpillow.pillowdrive.service;

import com.hyxpillow.pillowdrive.common.exception.PillowException;
import com.hyxpillow.pillowdrive.common.result.ResultCodeEnum;
import com.hyxpillow.pillowdrive.entity.UserEntity;
import com.hyxpillow.pillowdrive.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    JavaMailSender javaMailSender;

    public String login(String email, String password) throws PillowException {
        UserEntity userEntity = userMapper.findByEmail(email);
        if (userEntity == null) {
            throw new PillowException(ResultCodeEnum.USER_NOT_FOUND);
        } else if (!userEntity.getPassword().equals(password)) {
            throw new PillowException(ResultCodeEnum.LOGIN_PASSWD_FAIL);
        }
        // 登录成功，生成一个token，将token作为key, 用户id作为value写入redis
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        String value = userEntity.getId().toString();

        redisTemplate.opsForValue().set("login:" + key, value, 30, TimeUnit.MINUTES);
        return key;
    }

    public void register(String email, String password, String code) throws PillowException {
        if (password == null || password.length() < 6) {
            throw new PillowException(ResultCodeEnum.PASSWD_TOO_SHORT);
        }
        // 从redis读取刚刚获取的验证码
        String codeInRedis = redisTemplate.opsForValue().get("code:" + email);
        if (code == null || !code.equals(codeInRedis)) {
            throw new PillowException(ResultCodeEnum.CODE_NOT_EXIST);
        }
        redisTemplate.delete("code:" + email);
        try {
            int uid = userMapper.insertUser(email, password);
            // 创建用户的专属文件夹
            Path userPath = Paths.get("/home/hyxpillow/pillowdrive-data/" + uid);
            Files.createDirectories(userPath);
        } catch (Exception e) {
            throw new PillowException(ResultCodeEnum.USER_FOUND);
        }
    }

    public void resetPassword(String email, String password, String code) throws PillowException {
        if (password == null || password.length() < 6) {
            throw new PillowException(ResultCodeEnum.PASSWD_TOO_SHORT);
        }
        String codeInRedis = redisTemplate.opsForValue().get("code:" + email);
        if (code == null || !code.equals(codeInRedis)) {
            throw new PillowException(ResultCodeEnum.CODE_NOT_EXIST);
        }
        redisTemplate.delete("code:" + email);
        try {
            userMapper.updateUser(email, password);
        } catch (Exception e) {
            throw new PillowException(ResultCodeEnum.USER_NOT_FOUND);
        }
    }

    public void getCode(String email) throws PillowException {
        final String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(regex, email)) {
            throw new PillowException(ResultCodeEnum.EMAIL_FORMAT_FAIL);
        }

        String key = "code:" + email;
        long currentTime = System.currentTimeMillis();
        String value = String.format("%04d", (int)currentTime % 10000);
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("401887202@qq.com");
        message.setTo(email);
        message.setSubject("Pillowdrive 验证码");
        message.setText("您的验证码是" + value + " 有效期5分钟");
        javaMailSender.send(message);
    }

}
