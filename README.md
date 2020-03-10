# wechat-clj微信开发全家桶: 微信支付,网络授权,加密库等

- [wechat-clj微信开发全家桶: 微信支付,网络授权,加密库等](#wechat-clj%E5%BE%AE%E4%BF%A1%E5%BC%80%E5%8F%91%E5%85%A8%E5%AE%B6%E6%A1%B6-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98%E7%BD%91%E7%BB%9C%E6%8E%88%E6%9D%83%E5%8A%A0%E5%AF%86%E5%BA%93%E7%AD%89)
  - [Features](#features)
  - [Usage](#usage)
    - [小程序`wx.login`登陆使用后端的jscode2session获取微信信息](#%E5%B0%8F%E7%A8%8B%E5%BA%8Fwxlogin%E7%99%BB%E9%99%86%E4%BD%BF%E7%94%A8%E5%90%8E%E7%AB%AF%E7%9A%84jscode2session%E8%8E%B7%E5%8F%96%E5%BE%AE%E4%BF%A1%E4%BF%A1%E6%81%AF)
    - [微信服务号jsapi签名`wx.config` (用于微信网页支付和分享等的初始化)](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7jsapi%E7%AD%BE%E5%90%8Dwxconfig-%E7%94%A8%E4%BA%8E%E5%BE%AE%E4%BF%A1%E7%BD%91%E9%A1%B5%E6%94%AF%E4%BB%98%E5%92%8C%E5%88%86%E4%BA%AB%E7%AD%89%E7%9A%84%E5%88%9D%E5%A7%8B%E5%8C%96)
    - [oauth2服务号的二次授权](#oauth2%E6%9C%8D%E5%8A%A1%E5%8F%B7%E7%9A%84%E4%BA%8C%E6%AC%A1%E6%8E%88%E6%9D%83)
    - [微信服务号access_token访问的接口](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7access_token%E8%AE%BF%E9%97%AE%E7%9A%84%E6%8E%A5%E5%8F%A3)
    - [微信服务号的支付](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7%E7%9A%84%E6%94%AF%E4%BB%98)
    - [微信小程序的支付](#%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%9A%84%E6%94%AF%E4%BB%98)
  - [License](#license)

## Features

* [x] 微信支付的后端相关加密,包含小程序和微信服务号
* [x] 微信服务号的二次授权
* [x] 小程序的jscode2session和登录
* [x] 微信服务号获取用户信息的接口map
* [x] shadow-cljs 编写小程序的需要的npm库[mini-program-cljs](https://github.com/chanshunli/wechat-clj/tree/master/mini-program-cljs), 运用`wx.*`的方法时mock小程序方法(写自己的wx.*的spec解释器)在普通网页上Repl开发出来
* [ ] 微信小程序的UI组件库
* [ ] Hiccup生成微信小程序的前端页面
* [x] 微信小程序的支付
* [x] 微信服务号的支付
* [ ] 微信客户机器人和开发
* [ ] 签名和验证成功与否的工具函数或者解释器

## Usage

```clojure
[wechat "0.1.8-SNAPSHOT"]
```

### 小程序`wx.login`登陆使用后端的jscode2session获取微信信息

* 小程序端

``` bash
npm i mini-program-cljs
```

``` html
<button open-type="getUserInfo" bindgetuserinfo="getUserInfo"> 获取用户信息登陆</button>
```

```js

import { MiniCljs } from 'mini-program-cljs';

Page({
    getUserInfo: function(e) {
      MiniCljs.login(function(res) {console.log(res)}, e.detail.iv, e.detail.encryptedData)
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
      ;; do
      wx-user-data
      ;;
      )))
```

### 微信服务号jsapi签名`wx.config` (用于微信网页支付和分享等的初始化)
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

### oauth2服务号的二次授权

``` clojure
(ns your.ns
  (:require [wechat-clj.oauth2]))

;; TODO

```
### 微信服务号access_token访问的接口

``` clojure
(ns your.ns
  (:require [wechat-clj.public.core]))

;; TODO
```

### 微信服务号的支付

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

### 微信小程序的支付
* 后端API同服务号的微信支付的用法
* 前端小程序
```js
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
## License

Copyright © 2020 Steve Chan
