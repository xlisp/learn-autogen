(ns wechat-clj.wxpay.api
  (:import
   (java.util HashMap Map UUID)
   (java.io FileInputStream File)
   (com.github.wxpay.sdk WXPay WXPayConfig
     WXPayUtil WXPayConstants$SignType))
  (:require [clojure.string :as str]
            [wechat-clj.util :as util]))

(defn signature [{:keys [appid key]}
                 {:keys [nonceStr prepay_id timeStamp]}]
  (let [hash {"appId" appid
              "nonceStr" nonceStr
              "package" (str "prepay_id=" prepay_id)
              "signType" "MD5"
              "timeStamp" timeStamp}]
    (WXPayUtil/generateSignature (HashMap. hash) key WXPayConstants$SignType/MD5)))

(defn get-unix-second []
  (int (/ (System/currentTimeMillis) (Long. 1000))))

(defn read-wxpay-cert-byte [apiclient-cert]
  (let [file (File. apiclient-cert)
        cert-data (byte-array (.length file))]
    (do
      (doto (FileInputStream. file)
        (.read cert-data)
        (.close))
      (java.io.ByteArrayInputStream. cert-data))))

(defn wx-pay-config
  [appid mchid key wxpay-cert-byte]
  (reify
    WXPayConfig
    (getAppID [_] appid)
    (getMchID [_] mchid)
    (getKey [_] key)
    (getCertStream [_] wxpay-cert-byte)
    (getHttpConnectTimeoutMs [_] 8000)
    (getHttpReadTimeoutMs [_] 10000)))

(defn order-query
  [{:keys [appid mchid key]} {:keys [out_trade_no wxpay-cert-byte op-fn]}]
  (let [wxpay (WXPay. (wx-pay-config appid mchid key))
        data (HashMap. {"out_trade_no" out_trade_no})]
    (let [res (.orderQuery wxpay data)]
      (if (= (get res "result_code") "SUCCESS")
        (op-fn {:msg "ok" :res res})
        (op-fn {:msg "failure" :res res})))))

(defn unified-order
  [{:keys [appid mchid key]}
   {:keys [body total_fee trade_type openid out-trade-no op-fn cert callback-url]}]
  (let [wxpay (WXPay. (wx-pay-config appid mchid key cert))
        data (HashMap. {"body"             body
                        "out_trade_no"     out-trade-no
                        "device_info"      ""
                        "fee_type"         "CNY"
                        "total_fee"        (str total_fee)
                        "spbill_create_ip" "127.0.0.1"
                        "notify_url"       callback-url
                        "openid"           openid
                        "trade_type"       trade_type
                        "product_id"       ""})
        res (.unifiedOrder wxpay data)]
    (if (= (get res "result_code") "SUCCESS")
      (op-fn {:msg "ok" :res res :out_trade_no out-trade-no})
      (op-fn {:msg "failure" :res res}))))

(comment
  (refund {:total_fee "1" :refund_fee "1"
           :out_trade_no "2aed4b11e8864c522d2e0b72125df5f0"
           :out_refund_no "2a0d4b11e8864ce22d2e5b72125df5f1" :op-fn #(prn %)}))
(defn refund
  "微信退款"
  [{:keys [appid mchid key]}
   {:keys [total_fee refund_fee out_trade_no out_refund_no cert op-fn]}]
  (let [noncestr (str/replace (str (UUID/randomUUID)) "-" "")
        wxpay (WXPay. (wx-pay-config appid mchid key cert))
        data {"out_refund_no" out_refund_no
              "total_fee"     (str total_fee)
              "refund_fee"    (str refund_fee)
              "out_trade_no"  out_trade_no
              "nonce_str"     noncestr}
        signature (util/gen-signature data key)
        res (.refund wxpay (HashMap. (merge data {"sign" signature})))]
    (if (= (get res "result_code") "SUCCESS")
      (op-fn {:msg "ok" :res res :out_trade_no out_trade_no :out_refund_no out_refund_no})
      (op-fn {:msg "failure" :res res}))))

(defn refund-query
  "退款的查询"
  [{:keys [appid mchid key]}
   {:keys [out_trade_no cert op-fn]}]
  (let [wxpay (WXPay. (wx-pay-config appid mchid key cert))
        data (HashMap. {"out_trade_no" out_trade_no})]
    (let [res (.refundQuery wxpay data)]
      (if (= (get res "result_code") "SUCCESS")
        (op-fn {:msg "ok" :res res})
        (op-fn {:msg "failure" :res res})))))

(comment
  (transfers {:openid "dasdas312312dsalfdsaljk"
              :amount "1" :partner_trade_no "fc9f0f5e8c5d11e7943f7b4a29abcf7x"
              :op-fn #(prn %)}))
(defn transfer
  "企业付款
  * 给同一个实名用户付款，单笔单日限额2W/2W
  * 不支持给非实名用户打款
  * 一个商户同一日付款总额限额100W
  * 单笔最小金额默认为1元
  * 每个用户每天最多可付款10次，可以在商户平台--API安全进行设置"
  [{:keys [appid mchid key]}
   {:keys [amount openid partner_trade_no cert desc op-fn]}]
  (let [noncestr (str/replace (str (UUID/randomUUID)) "-" "")
        url "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers"
        wxpay (WXPay. (wx-pay-config appid mchid key cert))
        data {"nonce_str"        noncestr
              "mch_appid"        appid
              "desc"             desc
              "spbill_create_ip" "127.0.0.1"
              "amount"           amount
              "openid"           openid
              "check_name"       "NO_CHECK"
              "mchid"            mchid
              "partner_trade_no" partner_trade_no}
        signature (util/gen-signature data key)
        data-with-sig (assoc data "sign" signature)
        res (-> (.requestWithCert wxpay url data-with-sig 8000 10000)
              util/parse-string-xml
              util/get-hash-from-parsed-xml)]
    (if (= (:result_code res) "SUCCESS")
      (op-fn {:msg "ok" :res res :partner_trade_no partner_trade_no})
      (op-fn {:msg "failure" :res res}))))
