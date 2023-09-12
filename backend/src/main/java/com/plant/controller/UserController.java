package com.plant.controller;

import com.plant.constant.CodeMessage;
import com.plant.entity.Result;
import com.plant.pojo.User;
import com.plant.service.UserService;
import com.plant.utils.ValidateCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/login.do")
    public Result login(@RequestBody User loginUser) {
        if (StringUtils.isBlank(loginUser.getUsername()) || StringUtils.isBlank(loginUser.getPassword())) {
            return new Result(false, CodeMessage.CODE_401,"密码和用户名不可为空");
        }
        Result result = userService.login(loginUser);
        return result;
    }

    @RequestMapping("/register.do")
    public Result register(@RequestBody User registerUser) {
        if (StringUtils.isBlank(registerUser.getUsername()) || StringUtils.isBlank(registerUser.getPassword())) {
            return new Result(false, CodeMessage.CODE_401,"密码和用户名不可为空");
        }
        Result result = userService.register(registerUser);
        return result;
    }

    @RequestMapping("/sendMsg.do")
    public Result sendMsg(@RequestBody User user) {
        // 获取邮箱账号
        String email = user.getEmail();
        String subject = "智慧种植系统验证码";

        if (StringUtils.isNotEmpty(email)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            String context = "欢迎使用智慧种植系统，验证码为: " + code + ",五分钟内有效，请妥善保管!";

            // 真正地发送邮箱验证码
            userService.sendMsg(email, subject, context);


            // 验证码由保存到session 优化为 缓存到Redis中，并且设置验证码的有效时间为 5分钟
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

            return new Result(true,CodeMessage.CODE_200,"验证码发送成功");
        }
        return new Result(false,CodeMessage.CODE_400,"验证码发送失败");
    }

    @RequestMapping("/change.do")
    public Result change(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String code = map.get("code");
        String passWord = map.get("password");
        String authCode = (String)redisTemplate.opsForValue().get("email");
        User dbUser = userService.findByEmail(email);
        if(dbUser == null) {
            return new Result(false,CodeMessage.CODE_400,"该邮箱未注册账号");
        } else {
            if(authCode == null) {
                return new Result(false,CodeMessage.CODE_400,"验证码为空");
            }

            if(!authCode.equals(code)) {
                return new Result(false,CodeMessage.CODE_400,"验证码错误");
            }

            return userService.change(dbUser);
        }
    }

}
