(ns wechat-clj.wxpay)

(defn choose-wxpay
  [{:keys [appid time-stamp nonce-str prepay-id
           sign-type pay-sign success-fn]
    :or {sign-type "MD5"
         success-fn (fn [res] (js/alert (str "支付成功" res)))}}]
  (.chooseWXPay
    js/wx
    (clj->js {:appId appid
              :timeStamp time-stamp
              :nonceStr nonce-str
              :package (str "prepay_id=" prepay-id)
              :signType sign-type
              :paySign pay-sign
              :success success-fn})))
