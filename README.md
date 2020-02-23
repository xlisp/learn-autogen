# wechat-clj微信开发全家桶: 微信支付,网络授权,加密库等

## Features

* 微信支付的后端相关加密,包含小程序和微信服务号
* 微信服务号的二次授权和小程序的jscode2session
* 微信服务号获取用户信息的接口map
* shadow-cljs 编写小程序的需要的npm库`mini-program-cljs`, 运用`wx.*`的方法时mock小程序方法(写自己的wx.*的spec解释器)在普通网页上Repl开发出来 (参考: https://github.com/wechat-miniprogram/sm-crypto)

## Usage

```clojure
[wechat "0.1.0-SNAPSHOT"]

(ns my.ns
  (:require [wechat.core :refer :all]))

```

## License

Copyright © 2020 Steve Chan
