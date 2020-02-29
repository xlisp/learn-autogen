# wechat-clj微信开发全家桶: 微信支付,网络授权,加密库等

- [wechat-clj微信开发全家桶: 微信支付,网络授权,加密库等](#wechat-clj%E5%BE%AE%E4%BF%A1%E5%BC%80%E5%8F%91%E5%85%A8%E5%AE%B6%E6%A1%B6-%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98%E7%BD%91%E7%BB%9C%E6%8E%88%E6%9D%83%E5%8A%A0%E5%AF%86%E5%BA%93%E7%AD%89)
  - [Features](#features)
  - [Usage](#usage)
    - [小程序`wx.login`登陆使用后端的jscode2session获取微信信息](#%E5%B0%8F%E7%A8%8B%E5%BA%8Fwxlogin%E7%99%BB%E9%99%86%E4%BD%BF%E7%94%A8%E5%90%8E%E7%AB%AF%E7%9A%84jscode2session%E8%8E%B7%E5%8F%96%E5%BE%AE%E4%BF%A1%E4%BF%A1%E6%81%AF)
    - [微信服务号jsapi签名`wx.config` (用于微信网页支付和分享等的初始化)](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7jsapi%E7%AD%BE%E5%90%8Dwxconfig-%E7%94%A8%E4%BA%8E%E5%BE%AE%E4%BF%A1%E7%BD%91%E9%A1%B5%E6%94%AF%E4%BB%98%E5%92%8C%E5%88%86%E4%BA%AB%E7%AD%89%E7%9A%84%E5%88%9D%E5%A7%8B%E5%8C%96)
    - [小程序微信支付](#%E5%B0%8F%E7%A8%8B%E5%BA%8F%E5%BE%AE%E4%BF%A1%E6%94%AF%E4%BB%98)
    - [oauth2服务号的二次授权](#oauth2%E6%9C%8D%E5%8A%A1%E5%8F%B7%E7%9A%84%E4%BA%8C%E6%AC%A1%E6%8E%88%E6%9D%83)
    - [微信服务号access_token访问的接口](#%E5%BE%AE%E4%BF%A1%E6%9C%8D%E5%8A%A1%E5%8F%B7access_token%E8%AE%BF%E9%97%AE%E7%9A%84%E6%8E%A5%E5%8F%A3)
  - [License](#license)

## Features

* [x] 微信支付的后端相关加密,包含小程序和微信服务号
* [x] 微信服务号的二次授权
* [x] 小程序的jscode2session和登录
* [x] 微信服务号获取用户信息的接口map
* [x] shadow-cljs 编写小程序的需要的npm库[mini-program-cljs](https://github.com/chanshunli/wechat-clj/tree/master/mini-program-cljs), 运用`wx.*`的方法时mock小程序方法(写自己的wx.*的spec解释器)在普通网页上Repl开发出来
* [ ] 微信小程序的UI组件库
* [ ] Hiccup生成微信小程序的前端页面
* [ ] 微信小程序的支付
* [ ] 微信服务号的支付

## Usage

```clojure
[wechat "0.1.8-SNAPSHOT"]
```

### 小程序`wx.login`登陆使用后端的jscode2session获取微信信息

* 小程序端

``` bash
npm i mini-program-cljs
```

```js

var y = require("mini-program-cljs");

y.MiniCljs.login(function(res) {console.log(res)})

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
### 小程序微信支付

``` clojure
(ns yours.ns
  (:require [wechat-clj.wxpay.api :as wxpay]))

(def wxpay-cert-byte
  (future
    (wxpay/read-wxpay-cert-byte "apiclient_cert.p12")))

(defn miniprogram-unified-order
  [{:keys [body total-fee openid out-trade-no callback-url op-fn]}]
  (let [{:keys [appid mchid key secret]} wxpay-confg]
    (wxpay/unified-order
      wxpay-confg
      {:body body
       :total_fee total-fee
       :trade_type "NATIVE"
       :openid openid
       :out-trade-no out-trade-no
       :op-fn op-fn
       :cert @wxpay-cert-byte
       :callback-url callback-url})))
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

## License

Copyright © 2020 Steve Chan
