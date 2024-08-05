package com.hyxpillow.pillowdrive.mapper;

import com.hyxpillow.pillowdrive.entity.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT id, email, password FROM user WHERE email=#{email}")
    UserEntity findByEmail(@Param("email") String email);

    @Insert("INSERT INTO user(email, password) VALUES (#{email}, #{password})")
    int insertUser(@Param("email") String email, @Param("password") String password);

    @Update("UPDATE user set password = #{password} WHERE email = #{email}")
    int updateUser(@Param("email") String email, @Param("password") String password);
}
