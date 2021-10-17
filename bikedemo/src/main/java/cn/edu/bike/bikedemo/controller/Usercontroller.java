package cn.edu.bike.bikedemo.controller;

import cn.edu.bike.bikedemo.pojo.User;
import cn.edu.bike.bikedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Usercontroller {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/genCode")
    @ResponseBody
    public boolean genVerifyCode(String countryCode,String phoneNum) {
        boolean flag = userService.sendMsg(countryCode,phoneNum);
        System.out.println(phoneNum);
        return flag;
    }

    @RequestMapping("/user/verify")
    @ResponseBody
    public boolean verify(String phoneNum,String verifyCode) {
        return userService.verify(phoneNum,verifyCode);
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public boolean register(@RequestBody User user) { //POST请求 必须用requestbody把post的json数据转成字符串
        boolean flag = true;
         try {
             userService.register(user);
         }catch(Exception e){
             e.printStackTrace();
             flag = false;
         }
         return flag;
    }


    @RequestMapping("/user/deposite")
    @ResponseBody
    public boolean deposite(@RequestBody User user) {
        boolean flag=true;
        try {
            userService.update(user);
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @RequestMapping("/user/identify")
    @ResponseBody
    public boolean identify(@RequestBody User user) {
        boolean flag = true;
        try{
            userService.update(user);
        }catch(Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }
}
