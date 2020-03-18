# wechat-clj微信开发全家桶: 微信支付,小程序库,服务号开发,代码生成器等

## ClojureScript Repl开发微信小程序库体验, 爽呆了!!! => [成为开发者,请点击文字](https://github.com/chanshunli/mini-program-cljs-example)

![](https://github.com/chanshunli/wechat-clj/raw/master/cljs_repl_dev_miniprogram.gif)

- [wechat-clj微信开发全家桶: 微信支付,小程序库,服务号开发,代码生成器等](#wechat-clj%E5%BE%AE%E4%BF%A1%E5%BC%80%E5%8F%91%E5%85%A8%E5%AE%B6%E6%A1%B6-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%BA%93%E6%9C%8D%E5%8A%A1%E5%8F%B7%E5%BC%80%E5%8F%91%E4%BB%A3%E7%A0%81%E7%94%9F%E6%88%90%E5%99%A8%E7%AD%89)
  - [Features](#features)
  - [Usage](#usage)
  - [微信小程序](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F)
    - [小程序`wx.login`登陆使用后端的jscode2session获取微信信息](#%E5%B0%8F%E7%A8%8B%E5%BA%8Fwxlogin%E7%99%BB%E9%99%86%E4%BD%BF%E7%94%A8%E5%90%8E%E7%AB%AF%E7%9A%84jscode2session%E8%8E%B7%E5%8F%96%E5%BE%AE%E4%BF%A1%E4%BF%A1%E6%81%AF)
    - [异步的`wx.request`使用](#%E5%BC%82%E6%AD%A5%E7%9A%84wxrequest%E4%BD%BF%E7%94%A8)
    - [异步的`wx.NewRequest`使用](#%E5%BC%82%E6%AD%A5%E7%9A%84wxnewrequest%E4%BD%BF%E7%94%A8)
    - [`wx.*`类方法的调用方法](#wx%E7%B1%BB%E6%96%B9%E6%B3%95%E7%9A%84%E8%B0%83%E7%94%A8%E6%96%B9%E6%B3%95)
    - [`Page.this`传入类的调用方法](#pagethis%E4%BC%A0%E5%85%A5%E7%B1%BB%E7%9A%84%E8%B0%83%E7%94%A8%E6%96%B9%E6%B3%95)
    - [分页的组件使用](#%E5%88%86%E9%A1%B5%E7%9A%84%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)
    - [States工具类](#states%E5%B7%A5%E5%85%B7%E7%B1%BB)
    - [miniprogram-automator 模拟器测试工具使用](#miniprogram-automator-%E6%A8%A1%E6%8B%9F%E5%99%A8%E6%B5%8B%E8%AF%95%E5%B7%A5%E5%85%B7%E4%BD%BF%E7%94%A8)
    - [时间选择组件使用](#%E6%97%B6%E9%97%B4%E9%80%89%E6%8B%A9%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)
    - [图表组件使用](#%E5%9B%BE%E8%A1%A8%E7%BB%84%E4%BB%B6%E4%BD%BF%E7%94%A8)
    - [微信小程序的支付](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%9A%84%E6%94%AF%E4%BB%98)
  - [微信服务号](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7)
    - [基础支持-获取access_token: 定时器获取](#%E5%9F%BA%E7%A1%80%E6%94%AF%E6%8C%81-%E8%8E%B7%E5%8F%96access_token-%E5%AE%9A%E6%97%B6%E5%99%A8%E8%8E%B7%E5%8F%96)
    - [基础支持-获取微信服务器IP地址](#%E5%9F%BA%E7%A1%80%E6%94%AF%E6%8C%81-%E8%8E%B7%E5%8F%96%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%99%A8ip%E5%9C%B0%E5%9D%80)
    - [接收消息-验证消息真实性、接收普通消息、接收事件推送、接收语音识别结果](#%E6%8E%A5%E6%94%B6%E6%B6%88%E6%81%AF-%E9%AA%8C%E8%AF%81%E6%B6%88%E6%81%AF%E7%9C%9F%E5%AE%9E%E6%80%A7%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF%E6%8E%A5%E6%94%B6%E4%BA%8B%E4%BB%B6%E6%8E%A8%E9%80%81%E6%8E%A5%E6%94%B6%E8%AF%AD%E9%9F%B3%E8%AF%86%E5%88%AB%E7%BB%93%E6%9E%9C)
    - [发送消息-被动回复消息](#%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF-%E8%A2%AB%E5%8A%A8%E5%9B%9E%E5%A4%8D%E6%B6%88%E6%81%AF)
    - [发送消息-客服接口](#%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF-%E5%AE%A2%E6%9C%8D%E6%8E%A5%E5%8F%A3)
    - [发送消息-群发接口](#%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF-%E7%BE%A4%E5%8F%91%E6%8E%A5%E5%8F%A3)
    - [发送消息-模板消息接口](#%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF-%E6%A8%A1%E6%9D%BF%E6%B6%88%E6%81%AF%E6%8E%A5%E5%8F%A3)
    - [发送消息-一次性订阅消息接口](#%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF-%E4%B8%80%E6%AC%A1%E6%80%A7%E8%AE%A2%E9%98%85%E6%B6%88%E6%81%AF%E6%8E%A5%E5%8F%A3)
    - [用户管理-用户分组管理](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E7%94%A8%E6%88%B7%E5%88%86%E7%BB%84%E7%AE%A1%E7%90%86)
    - [用户管理-设置用户备注名](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E8%AE%BE%E7%BD%AE%E7%94%A8%E6%88%B7%E5%A4%87%E6%B3%A8%E5%90%8D)
    - [用户管理-获取用户基本信息](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF)
    - [用户管理-获取用户列表](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E5%88%97%E8%A1%A8)
    - [用户管理-获取用户地理位置](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE)
    - [用户管理-网页授权获取用户openid/用户基本信息: oauth2服务号的二次授权](#%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86-%E7%BD%91%E9%A1%B5%E6%8E%88%E6%9D%83%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7openid%E7%94%A8%E6%88%B7%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF-oauth2%E6%9C%8D%E5%8A%A1%E5%8F%B7%E7%9A%84%E4%BA%8C%E6%AC%A1%E6%8E%88%E6%9D%83)
    - [推广支持-生成带参数二维码](#%E6%8E%A8%E5%B9%BF%E6%94%AF%E6%8C%81-%E7%94%9F%E6%88%90%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81)
    - [推广支持-长链接转短链接口](#%E6%8E%A8%E5%B9%BF%E6%94%AF%E6%8C%81-%E9%95%BF%E9%93%BE%E6%8E%A5%E8%BD%AC%E7%9F%AD%E9%93%BE%E6%8E%A5%E5%8F%A3)
    - [界面丰富-自定义菜单](#%E7%95%8C%E9%9D%A2%E4%B8%B0%E5%AF%8C-%E8%87%AA%E5%AE%9A%E4%B9%89%E8%8F%9C%E5%8D%95)
    - [素材管理-素材管理接口](#%E7%B4%A0%E6%9D%90%E7%AE%A1%E7%90%86-%E7%B4%A0%E6%9D%90%E7%AE%A1%E7%90%86%E6%8E%A5%E5%8F%A3)
    - [智能接口-语义理解接口](#%E6%99%BA%E8%83%BD%E6%8E%A5%E5%8F%A3-%E8%AF%AD%E4%B9%89%E7%90%86%E8%A7%A3%E6%8E%A5%E5%8F%A3)
    - [多客服-获取多客服消息记录、客服管理](#%E5%A4%9A%E5%AE%A2%E6%9C%8D-%E8%8E%B7%E5%8F%96%E5%A4%9A%E5%AE%A2%E6%9C%8D%E6%B6%88%E6%81%AF%E8%AE%B0%E5%BD%95%E5%AE%A2%E6%9C%8D%E7%AE%A1%E7%90%86)
    - [微信支付接口](#%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98%E6%8E%A5%E5%8F%A3)
    - [微信小店接口](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E5%BA%97%E6%8E%A5%E5%8F%A3)
    - [微信卡券接口](#%E5%BE%AE%E4%BF%A1%E5%8D%A1%E5%88%B8%E6%8E%A5%E5%8F%A3)
    - [微信设备功能接口](#%E5%BE%AE%E4%BF%A1%E8%AE%BE%E5%A4%87%E5%8A%9F%E8%83%BD%E6%8E%A5%E5%8F%A3)
    - [微信发票接口](#%E5%BE%AE%E4%BF%A1%E5%8F%91%E7%A5%A8%E6%8E%A5%E5%8F%A3)
    - [微信JS-SDK-基础接口: 微信服务号jsapi签名`wx.config` (用于微信网页支付和分享等的初始化)](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%9F%BA%E7%A1%80%E6%8E%A5%E5%8F%A3-%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7jsapi%E7%AD%BE%E5%90%8Dwxconfig-%E7%94%A8%E4%BA%8E%E5%BE%AE%E4%BF%A1%E7%BD%91%E9%A1%B5%E6%94%AF%E4%BB%98%E5%92%8C%E5%88%86%E4%BA%AB%E7%AD%89%E7%9A%84%E5%88%9D%E5%A7%8B%E5%8C%96)
    - [微信JS-SDK-分享接口](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%88%86%E4%BA%AB%E6%8E%A5%E5%8F%A3)
    - [微信JS-SDK-图像接口](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%9B%BE%E5%83%8F%E6%8E%A5%E5%8F%A3)
    - [微信JS-SDK-音频接口](#%E5%BE%AE%E4%BF%A1js-sdk-%E9%9F%B3%E9%A2%91%E6%8E%A5%E5%8F%A3)
    - [微信JS-SDK-智能接口](#%E5%BE%AE%E4%BF%A1js-sdk-%E6%99%BA%E8%83%BD%E6%8E%A5%E5%8F%A3)
    - [微信JS-SDK-设备信息](#%E5%BE%AE%E4%BF%A1js-sdk-%E8%AE%BE%E5%A4%87%E4%BF%A1%E6%81%AF)
    - [微信JS-SDK-地理位置](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE)
    - [微信JS-SDK-界面操作](#%E5%BE%AE%E4%BF%A1js-sdk-%E7%95%8C%E9%9D%A2%E6%93%8D%E4%BD%9C)
    - [微信JS-SDK-微信扫一扫](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%BE%AE%E4%BF%A1%E6%89%AB%E4%B8%80%E6%89%AB)
    - [微信JS-SDK-微信小店](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%BE%AE%E4%BF%A1%E5%B0%8F%E5%BA%97)
    - [微信JS-SDK-微信卡券](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%BE%AE%E4%BF%A1%E5%8D%A1%E5%88%B8)
    - [微信JS-SDK-微信支付](#%E5%BE%AE%E4%BF%A1js-sdk-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98)
  - [成为开发者](#%E6%88%90%E4%B8%BA%E5%BC%80%E5%8F%91%E8%80%85)
    - [基于miniprogram-automator开发ClojureScript小程序库](#%E5%9F%BA%E4%BA%8Eminiprogram-automator%E5%BC%80%E5%8F%91clojurescript%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%BA%93)
    - [贡献wechat-clj代码,Pull Request](#%E8%B4%A1%E7%8C%AEwechat-clj%E4%BB%A3%E7%A0%81pull-request)
  - [License](#license)

## Features

* [x] 微信支付的后端相关加密,包含小程序和微信服务号
* [x] 微信服务号的二次授权
* [x] 小程序的jscode2session和登录
* [x] 微信服务号获取用户信息的接口map
* [x] shadow-cljs 编写小程序的需要的npm库[mini-program-cljs](https://github.com/chanshunli/wechat-clj/tree/master/mini-program-cljs)
* [x] 微信小程序的支付
* [x] 微信服务号的支付
* [ ] 微信小程序的UI组件库
* [ ] Hiccup生成微信小程序的前端页面: [Elisp混合clomacs生成方案](https://github.com/chanshunli/emacs_spark_nlp/blob/master/elisp/jim-emmet.el) 和 纯Clojure生成wxml和wxss代码方案
* [ ] 微信客户机器人和开发
* [ ] 签名,加解密和验证成功与否的工具函数或者spec解释器: spec出来具体很细的签名哪一部分错了,前端还是后端,还是排序还是大小写,缺了哪个字段等,大大减少验证签名的时间
* [ ] 给每一个调用的前后端函数写Spec,严格化工程
* [ ] 基于[miniprogram-automator](https://developers.weixin.qq.com/miniprogram/dev/devtools/auto/)完善ClojureScript和Shadow开发微信小程序库的流程和自动化工具,测试链(Repl式的开发体验)
* [ ] 基于小程序的canvas画图类库: 可以快速写出"头像加国旗"类的小程序来

## Usage

* Clojure(Script)前后端库[wechat-clj](https://clojars.org/wechat-clj)
```clojure
[wechat-clj "0.2.2-SNAPSHOT"]
```
* [微信小程序的cljs库mini-program-cljs](https://www.npmjs.com/package/mini-program-cljs): 安装`mini-program-cljs`

```sh

npm i mini-program-cljs

## Mac的微信开发者工具再编译:
/Applications/wechatwebdevtools.app/Contents/MacOS/cli --build-npm ~/YourWechatProject

```

``` javascript
import { MiniCljs } from 'mini-program-cljs';

MiniCljs.alert("Hello, mini-program-cljs!");
```


## [微信小程序](https://developers.weixin.qq.com/miniprogram/dev/api/)

### 小程序`wx.login`登陆使用后端的jscode2session获取微信信息

``` html
<button open-type="getUserInfo" bindgetuserinfo="getUserInfo"> 获取用户信息登陆</button>
```

```js
Page({
    getUserInfo: function(e) {
      MiniCljs.login({successFn: function(res) {console.log(res)},
                      iv: e.detail.iv,
                      encryptedData: e.detail.encryptedData})
    }
})

//=> res: 将res的内容传递给后端的接口即可获取用户的信息(openid等)
{code: "043Ndldz0WVpcc1Cqpcz03Xgdz0...."
 encryptedData: "NxmCRAyhhMT2jzdcu012VJznC6HH0H....."
 iv: "jbjTusiIz2tfzt1ddU..=="}

```
* 后端的jscode2session的API

```clojure
(ns your.ns
  (:require [wechat-clj.jscode2session :refer [get-jscode2session decrypt-wxdata]]))

(defn jscode2session-api [{{:keys [jscode encrypted_data iv]} :params}]
  (let [{:keys [appid secret]} {:appid "小程序的appid" :secret "小程序的secret"}
        {:keys [errcode errmsg hints session_key expires_in openid unionid]}
        (get-jscode2session {:jscode jscode :wxapp-key wxapp-key :op-fn #(identity %)})]
    (let [wx-user-data
          (decrypt-wxdata {:encrypted_data encrypted_data
                           :session_key session_key
                           :iv iv})]
      ;; 加入表存储你需要的微信用户信息
      wx-user-data
      ;;
      )))
```

### 异步的`wx.request`使用

``` javascript

MiniCljs.request(
  {url: 'https://www.test.com/login/jscode2session',
   method: "GET",
   data: {encrypted_data: res.encryptedData,
   iv: res.iv,
   jscode: res.code}}).then(res => {
   console.log("登陆返回数据");
   console.log(res);
   if(res.statusCode == 200){
     // ...
   } else {
     // ...
   }
   }).catch(err => {
   MiniCljs.alert(err.message)
   })
```

### 异步的`wx.NewRequest`使用

``` javascript
;; TODO
```

### `wx.*`类方法的调用方法

``` javascript
;; TODO
```

### `Page.this`传入类的调用方法

``` javascript
;; TODO
MiniCljs.testFun(this, arg1 ...)
```

### 分页的组件使用
``` javascript
;; TODO
```
### States工具类
``` javascript
;; TODO
```
### miniprogram-automator 模拟器测试工具使用
``` javascript
;; TODO
```
### 时间选择组件使用
``` javascript
;; TODO
```
### 图表组件使用
``` javascript
;; TODO
```

### 微信小程序的支付
* 后端API同服务号的微信支付的用法
* 前端小程序
```js
// TODO: 需要封装
wx.requestPayment({
    'timeStamp': res.data.timeStamp,
    'nonceStr': res.data.nonceStr,
    'package': "prepay_id=" + res.data.prepayId,
    'signType': 'MD5',
    'paySign': res.data.paySign,
    'success':function(res){
        console.log(res);
        MiniCljs.alert("支付成功");
    },
    'fail':function(res){
        console.log(res);
        MiniCljs.alert("支付失败");
    },
    'complete':function(res){
        console.log(res);
        MiniCljs.alert("支付完成");
    }
})
```

## [微信服务号](https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Explanation_of_interface_privileges.html)

### 基础支持-获取access_token: 定时器获取
``` clojure
(ns your.ns
  (:require [wechat-clj.public.core]))

;; TODO
```

### 基础支持-获取微信服务器IP地址
```clojure
;; TODO
```

### 接收消息-验证消息真实性、接收普通消息、接收事件推送、接收语音识别结果
```clojure
;; TODO
```

### 发送消息-被动回复消息
```clojure
;; TODO
```

### 发送消息-客服接口
```clojure
;; TODO
```

### 发送消息-群发接口
```clojure
;; TODO
```

### 发送消息-模板消息接口
```clojure
;; TODO
```

### 发送消息-一次性订阅消息接口
```clojure
;; TODO
```

### 用户管理-用户分组管理
```clojure
;; TODO
```

### 用户管理-设置用户备注名
```clojure
;; TODO
```

### 用户管理-获取用户基本信息
```clojure
;; TODO
```

### 用户管理-获取用户列表
```clojure
;; TODO
```

### 用户管理-获取用户地理位置
```clojure
;; TODO
```

### 用户管理-网页授权获取用户openid/用户基本信息: oauth2服务号的二次授权
``` clojure
(ns your.ns
  (:require [wechat-clj.oauth2]))

;; TODO

```

### 推广支持-生成带参数二维码
```clojure
;; TODO
```

### 推广支持-长链接转短链接口
```clojure
;; TODO
```

### 界面丰富-自定义菜单
```clojure
;; TODO
```

### 素材管理-素材管理接口
```clojure
;; TODO
```

### 智能接口-语义理解接口
```clojure
;; TODO
```

### 多客服-获取多客服消息记录、客服管理
```clojure
;; TODO
```

### 微信支付接口
```clojure
;; TODO
```

### 微信小店接口
```clojure
;; TODO
```

### 微信卡券接口
```clojure
;; TODO
```

### 微信设备功能接口
```clojure
;; TODO
```

### 微信发票接口
```clojure
;; TODO
```

### 微信JS-SDK-基础接口: 微信服务号jsapi签名`wx.config` (用于微信网页支付和分享等的初始化)

* 后端接口获取或模板嵌入html携带jsapi的签名信息

```clojure
(ns your.ns
  (:require [wechat-clj.wxpay.public :as wx-public]))

(defn url-signature [url]
  (wx-public/url-signature
    {:appid {:appid "服务号的appid" :secret "服务号的secret"}
     :url url
     :debug true
     :official-account-jsapi-ticket "服务号的jsapi ticket: 详情见wechat-clj.public.core的文档"}))

(defn get-wx-jsapi-signature [request]
   (let [request-url (str
                       "http://your.domain"
                       (:uri request)
                       (wx-public/hash-params->url
                         (:params req)))]
     {:signature (-> request-url
                      url-signature
                      json/generate-string)}))
```
* 前端cljs获取后端的签名`signature`

```clojure
(ns your.ns
  (:require [wechat-clj.core :refer [wx-jsapi-init]]))

(wx-jsapi-init signature)

```

### 微信JS-SDK-分享接口
```clojure
;; TODO
```

### 微信JS-SDK-图像接口
```clojure
;; TODO
```

### 微信JS-SDK-音频接口
```clojure
;; TODO
```

### 微信JS-SDK-智能接口
```clojure
;; TODO
```

### 微信JS-SDK-设备信息
```clojure
;; TODO
```

### 微信JS-SDK-地理位置
```clojure
;; TODO
```

### 微信JS-SDK-界面操作
```clojure
;; TODO
```

### 微信JS-SDK-微信扫一扫
```clojure
;; TODO
```

### 微信JS-SDK-微信小店
```clojure
;; TODO
```

### 微信JS-SDK-微信卡券
```clojure
;; TODO
```

### 微信JS-SDK-微信支付

* 后端支付的接口的用法(假设用MD5签名)

``` clojure
(ns your.ns
  (:require [wechat-clj.wxpay.api :as wxpay]))

;; 读取微信支付的cert证书
(def wxpay-cert-byte
  (future
    (wxpay/read-wxpay-cert-byte "apiclient_cert.p12")))

(let [res (wxpay/unified-order
            wxpay-confg
            {:body "测试支付"
             :total_fee 1
             :trade_type "JSAPI"
             :openid "你的微信openid"
             :out-trade-no "321320989890312(交易id)"
             :op-fn identity
             :cert @wxpay-cert-byte
             :callback-url "https://支付成功回调的api接口"})
      ;;res正确返回的样子 => {:msg "ok", :res {"nonce_str" "6So2RRrVGKuBSxxxx", "code_url" "weixin://wxpay/bizpayurl?pr=2KEuxxx", "appid" "wxcxxxxx1121", "sign" "B4ACAE399E2307252xxxxx11", "trade_type" "JSAPI", "return_msg" "OK", "result_code" "SUCCESS", "mch_id" "13125434531", "return_code" "SUCCESS", "prepay_id" "wx022007560531243142423317900"}, :out_trade_no "aasdas321312asdds3123"}
      time-stamp (str (wxpay/get-unix-second))]

  ;; 注意: 微信之后的签名下单,默认为MD5签名 # 前端wx.chooseWXPay方法也会验证一次后端的签名是否一致
  (let [signature
        (wxpay/signature
          {:appid "服务号appid" :key "微信支付的key"}
          {:nonceStr (get (:res res) "nonce_str")
           :prepay_id (get (:res res) "prepay_id")
           :timeStamp time-stamp})]
    ;; 最后API返回给前端wx.chooseWXPay支付需要的信息
    {:timeStamp time-stamp
     :nonceStr nonce_str
     :prepayId prepay_id
     :paySign signature}
    )
  )

```

* 微信支付成功的回调XML解析,更新微信支付的状态机

``` clojure
(ns your.ns
  (:require [wechat-clj.util :as wechat-u]))

(defn your-wxpay-callback
  [request]
  (let [req-xml-body (slurp (:body request))
        {:keys [out_trade_no return_code] :as params} (wechat-u/parse-wxpay-callback-xml req-xml-body)]
        ;; 在数据库中将订单号的对应的状态设置为支付成功
        ;; ===>>> req-xml-body 样例
        ;;<xml><appid><![CDATA[wx321sde321]]></appid>
        ;;<bank_type><![CDATA[GDB_CREDIT]]></bank_type>
        ;;<cash_fee><![CDATA[1]]></cash_fee>
        ;;<fee_type><![CDATA[CNY]]></fee_type>
        ;;<is_subscribe><![CDATA[Y]]></is_subscribe>
        ;;<mch_id><![CDATA[15das321321]]></mch_id>
        ;;<nonce_str><![CDATA[bd650baa321321a2d02c5e]]></nonce_str>
        ;;<openid><![CDATA[o3Nbh0j0dasdas321321Z_x8Tq8s]]></openid>
        ;;<out_trade_no><![CDATA[135b652e27321321das5393b2d910a3cc]]></out_trade_no>
        ;;<result_code><![CDATA[SUCCESS]]></result_code>
        ;;<return_code><![CDATA[SUCCESS]]></return_code>
        ;;<sign><![CDATA[4E973EAA376203213210dasdas20035C]]></sign>
        ;;<time_end><![CDATA[201912263213249]]></time_end>
        ;;<total_fee>1</total_fee>
        ;;<trade_type><![CDATA[JSAPI]]></trade_type>
        ;;<transaction_id><![CDATA[4200000321321d32132105020]]></transaction_id>
        ;;</xml>
        ))
```

* 前端支付调用的用法(假设用MD5签名)

```clojure
(ns your.ns
  (:require [wechat-clj.wxpay :as wxpay]))

;; 下面的四个参数都是后端传过来的
(wxpay/choose-wxpay
  {:time-stamp time-stamp
   :nonce-str nonce-str
   :prepay-id prepay-id
   :pay-sign pay-sign})

```

## 成为开发者

### 基于[miniprogram-automator](https://developers.weixin.qq.com/miniprogram/dev/devtools/auto/)开发ClojureScript小程序库

``` clojure
;; TODO
```

### 贡献wechat-clj代码,Pull Request

``` clojure
;; TODO
```

## License

Copyright © 2020 Steve Chan
