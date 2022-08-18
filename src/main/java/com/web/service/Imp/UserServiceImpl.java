package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.UserDao;
import com.web.domain.User;
import com.web.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
