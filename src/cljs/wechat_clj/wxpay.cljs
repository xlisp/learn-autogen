(ns wechat-clj.wxpay)

(defn choose-wxpay
  [{:keys [time-stamp nonce-str prepay-id
           sign-type pay-sign success-fn]
    :or {sign-type "MD5"
         success-fn (fn [res] (js/alert (str "支付成功" res)))
         cencel-fn (fn [res] (js/alert (str "支付取消" res)))
         fail-fn (fn [res] (js/alert (str "支付失败" res)))}}]
  (.chooseWXPay
    js/wx
    (clj->js {:timestamp time-stamp
              :nonceStr nonce-str
              :signType sign-type
              :package (str "prepay_id=" prepay-id)
              :paySign pay-sign
              :success success-fn
              :cencel cencel-fn
              :fail fail-fn})))
