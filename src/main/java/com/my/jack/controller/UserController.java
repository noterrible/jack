package com.my.jack.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.jack.common.BaseContext;
import com.my.jack.common.R;
import com.my.jack.utils.SendEmail;
import com.my.jack.entity.ShoppingCart;
import com.my.jack.entity.User;
import com.my.jack.service.ShoppingCartService;
import com.my.jack.service.UserService;
import com.my.jack.utils.UserNameUtil;
import com.my.jack.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Api(tags="用户相关接口")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    @ApiOperation("发送验证码接口")
    public R<String> sendMsg(@RequestBody User user/*HttpSession session*/) {
        //获取手机号
        String email = user.getQqEmail();
        if (email != null) {
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("打印验证码:" + code);
            //采用邮箱发送验证码
            try {
                SendEmail.sendMassage(email,code);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //调用阿里云接口发送短信
            //SMSUtils.sendMessage("瑞吉外卖",phone,code);
            //生成验证码保存到session
            /*项目优化
            取消session缓存验证码，用redis实现缓存
            session.setAttribute(phone, code);*/
            //设置缓存验证码，设置有效期为5分钟
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return R.success("已成功发送验证码");
        }
        return R.error("发送失败");
    }

    @PostMapping("/login")
    @ApiOperation(value="用户登录接口")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取邮箱
        String qqEmail = map.get("email").toString();

        //获取验证码
        String code = map.get("code").toString();
        //从session获取保存验证码
        /*项目优化
        取消sesion获取验证码，使用redis获取
        Object codeInSession = session.getAttribute(phone);*/
        //从redis获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(qqEmail);
        //验证码比较
        if (codeInSession != null && codeInSession.equals(code)) {
            //核对正确登录成功
            //新用户自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getQqEmail, qqEmail);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setQqEmail(qqEmail);
                user.setName(UserNameUtil.getStringRandom(8));
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            //用户登陆成功,删除验证码
            redisTemplate.delete(qqEmail);
            return R.success(user);
        }
        if(codeInSession==null)
            return R.error("请获取验证码");
        return R.error("验证码或邮箱号错误");
    }

    @PostMapping("/loginout")
    @ApiOperation(value="用户登出接口")
    public R<String> loginout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        //退出登录清空购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("退出登录成功");
    }
}
