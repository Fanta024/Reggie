package com.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.web.common.R;
import com.web.domain.User;
import com.web.service.UserService;
import com.web.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMessage(@RequestBody User user, HttpSession httpSession){
        String phone = user.getPhone();


        if(!StringUtils.isEmpty(phone)){    //这里应该用commons-lang提供的
            //获取验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用api发送短信
//            SMSUtils.sendMessage(xxxx);

            //保存code 到session
            httpSession.setAttribute(phone,code);
            return R.success(null,"发送成功");
        }

        return R.error("发送失败");
    }
    @PostMapping("/login")
    R<User> login(@RequestBody Map map,HttpSession httpSession){
        log.info("{}",map);

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        String  codeInSession = httpSession.getAttribute(phone).toString();
        if (codeInSession!=null && code.equals(codeInSession)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user,"登录成功");

        }
        return R.error("验证码错误");
    }

}
