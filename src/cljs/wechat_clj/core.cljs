(ns wechat-clj.core
  (:require [wechat-clj.util]
            [wechat-clj.wxpay]
            [wechat-clj.jsapi]))

(defn ^:export wx-jsapi-init [signatures]
  (.config js/wx
    (clj->js signatures)))
