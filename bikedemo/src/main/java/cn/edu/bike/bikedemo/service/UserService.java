package cn.edu.bike.bikedemo.service;

import cn.edu.bike.bikedemo.pojo.User;

public interface UserService {
    boolean sendMsg(String countryCode, String phoneNum);

    boolean verify(String phoneNum, String verifyCode);

    void register(User user);

    void update(User user);
}
