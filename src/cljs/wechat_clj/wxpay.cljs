(ns wechat-clj.wxpay)

(defn ^:export wx-share-init [signatures]
  (.config js/wx
    (clj->js signatures)))

(defn choose-wxpay [args]
  (.chooseWXPay
    js/wx
    (clj->js args)))
