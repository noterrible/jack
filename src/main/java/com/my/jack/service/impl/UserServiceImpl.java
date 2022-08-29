package com.my.jack.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.jack.entity.User;
import com.my.jack.mapper.UserMapper;
import com.my.jack.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
