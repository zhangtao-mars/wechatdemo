package com.mars.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private WxMpService wxOpenService;

    @GetMapping("/qrAuth")
    public String qrAuth(@RequestParam("returnUrl") String returnUrl) {
        String url = "http://test.ewider.com.cn/wechatdemo/auth/qrUserInfo";
        String redirectUrl = wxOpenService.oauth2buildAuthorizationUrl(url,
                                                                        WxConsts.QrConnectScope.SNSAPI_LOGIN,
                                                                        URLEncoder.encode(returnUrl));

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
            log.info("【微信页面授权】 openID={}", wxMpOAuth2AccessToken.getOpenId());
        } catch (WxErrorException e) {
            log.error("【微信页面授权】 获取openID异常：{}", e);
        }

        return "redirect:" + returnUrl + "?openId=" + wxMpOAuth2AccessToken.getOpenId();
    }

    @ResponseBody
    @GetMapping("test")
    public String test() {
        return "test";
    }
}
