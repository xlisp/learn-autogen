(ns wechat-clj.wxpay.api
  (:import
   (java.util HashMap Map)
   (java.io FileInputStream File)
   (com.github.wxpay.sdk WXPay WXPayConfig
     WXPayUtil WXPayConstants$SignType)))

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
