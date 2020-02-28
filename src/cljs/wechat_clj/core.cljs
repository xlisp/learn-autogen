(ns wechat-clj.core
  (:require [wechat-clj.wxpay]))

(defn ^:export version [] "0.1.7")

(defn test-fun [title]
  (js/console.log title))
