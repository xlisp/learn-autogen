(ns wechat-clj.public.core
  (:require [clj-http.client :as client]
            [cheshire.core :as cjson]))

(defn invoke-wx-api-with-cfg-by-get
  "微信调用接口辅助函数get"
  [cmd form-params]
  (let [wx-api-url (format "https://api.weixin.qq.com/cgi-bin/%s" cmd)
        get-data {:accept :json :query-params form-params}]
    (let [resp (client/get wx-api-url get-data)]
      (cjson/parse-string (:body resp)))))

(defn invoke-wx-api-with-cfg
  "微信调用接口辅助函数
  cmd 为接口名,如 menu/create
  form-params 为map
  返回json解析的map"
  [access-token cmd form-params]
  (let [wx-api-url (format "https://api.weixin.qq.com/cgi-bin/%s?access_token=%s" cmd access-token)
        post-data {:form-params form-params
                   :content-type :json}]
    (let [resp (client/post wx-api-url post-data)]
      (cjson/parse-string (:body resp)))))

(defn get-wxuser-list [access-token]
  (-> (invoke-wx-api-with-cfg access-token "user/get" {})
    (get "data")
    (get "openid")))

(defn get-wxuser-info [{:keys [openid access-token]}]
  (invoke-wx-api-with-cfg-by-get
    "user/info"
    {:access_token access-token
     :openid openid :lang "zh_CN"}))

(defn print-all-wx-user [access-token]
  (doseq [openid (get-wxuser-list access-token)]
    (prn  (get-wxuser-info {:openid openid :access-token access-token}))))

(comment
  (get-wxuser-list "official-account-token")
  (get-wxuser-info {:openid  "opb8V1Q7oNvSjfQ-tCdasdas321321" :access-token "official-account-token"})
  (print-all-wx-user "official-account-token"))
