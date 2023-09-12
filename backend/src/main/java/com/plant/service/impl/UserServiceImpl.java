package com.plant.service.impl;

import com.plant.constant.CodeMessage;
import com.plant.dao.UserDao;
import com.plant.entity.Result;
import com.plant.pojo.User;
import com.plant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Result login(User loginUser) {
        User queryUser = userDao.findByUsername(loginUser.getUsername());
        if (queryUser == null) {
            return new Result(false, CodeMessage.CODE_400,"登录失败，用户名不存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(loginUser.getPassword(), queryUser.getPassword());
        if (!matches) {
            return new Result(false, CodeMessage.CODE_400, "登录失败，密码错误");
        }
        return new Result(true, CodeMessage.CODE_200, "登录成功","User");
    }

    @Override
    public Result register(User registerUser) {
        User dbUser = userDao.findByUsername(registerUser.getUsername());
        if (dbUser != null) {
            return new Result(false, CodeMessage.CODE_400, "注册失败,用户名已经存在");
        }

        //Encrypted Passwords
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode(registerUser.getPassword());
        registerUser.setPassword(encoderPassword);

        userDao.register(registerUser);

        return new Result(true, CodeMessage.CODE_200, "注册成功");
    }

    @Value("${spring.mail.username}")
    private String from;   // 邮件发送人

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMsg(String to, String subject, String context) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(context);

        // 真正的发送邮件操作，从 from到 to
        mailSender.send(mailMessage);
    }

    @Override
    public Result change(User user) {
        userDao.change(user);
       return new Result(true,CodeMessage.CODE_500,"修改成功");
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

}
