package cn.edu.bike.bikedemo.service;

import cn.edu.bike.bikedemo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean sendMsg(String countryCode, String phoneNum) {

        //从redis读取id和key
        String SecretID= stringRedisTemplate.opsForValue().get("SecretID");
        String SecretKey= stringRedisTemplate.opsForValue().get("SecretKey");

        //生成1个随机数字4位
        String code= (int)((Math.random()*9+1)*1000)+"";

        //调用腾讯云短信云api
        try{
            Credential cred = new Credential(SecretID, SecretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {"+"+countryCode+phoneNum};

            //将手机号作为key 验证码作为value 保存到redis中
            stringRedisTemplate.opsForValue().set(phoneNum,code,300, TimeUnit.SECONDS);

            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId("1400584879");
            req.setSignName("我的笔记园分享");
            req.setTemplateId("1159811");

            String[] templateParamSet1 = {code};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean verify(String phoneNum, String verifyCode) {
        boolean flag = false;
        //从redis中找对应的验证码
        String code = stringRedisTemplate.opsForValue().get(phoneNum);
        if(code!=null && code.equals(verifyCode)) {
            flag=true;
        }
        return flag;
    }



    @RequestMapping("/user/register")
    @ResponseBody
    public void register(User user) {

        mongoTemplate.insert(user);
        //调用mongodb的模板 保存用户数据
       // return false;
    }

    @Override
    public void update(User user) {
        //如果不存在就插入 存在就更新
//        mongoTemplate.insert(user);
        Update up = new Update();
        if(user.getDeposite()!=null) {
            up.set("deposite",user.getDeposite());
        }
        if(user.getStatus()!=null) {
            up.set("status",user.getStatus());
        }
        if(user.getName()!=null) {
            up.set("name",user.getName());
        }
        if(user.getIdNum()!=null) {
            up.set("idNum",user.getIdNum());
        }

        mongoTemplate.updateFirst(
                new Query(Criteria.where("phoneNum").is(user.getPhoneNum())),
                up,
                User.class);

    }
}
