(ns wechat-clj.oauth2
  (:require [clj-http.client :as client]
            [cheshire.core :as cjson]))

(defn generate-oauth2-url
  [{:keys [url appid]}]
  (let [encode-url (java.net.URLEncoder/encode url)
        oauth2-url
        (fn [url]
          (str "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
            appid  "&redirect_uri=" url
            "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"))]
    (oauth2-url encode-url)))

(defn get-oauth2-access-token [{:keys [appid appsecret code op-fn]}]
  (let [access-token-url "https://api.weixin.qq.com/sns/oauth2/access_token"
        userinfo-url "https://api.weixin.qq.com/sns/userinfo"
        {:keys [errcode errmsg access_token openid unionid] :as token-res}
        (-> access-token-url
          (client/get {:query-params
                       {:appid      appid
                        :secret     appsecret
                        :code       code
                        :grant_type "authorization_code"}})
          :body (cjson/parse-string true))]
    (op-fn token-res)))

(defn get-oauth2-userinfo
  [{:keys [op-fn access_token openid]}]
  (let [userinfo-url "https://api.weixin.qq.com/sns/userinfo"]
    (let [{:keys [sex nickname city headimgurl openid
                  language province country unionid privilege]
           :as wx-user-data}
          (-> userinfo-url
            (client/get {:query-params
                         {:access_token access_token
                          :openid       openid}})
            :body (cjson/parse-string true))]
      (op-fn wx-user-data))))

(defn get-wxuserinfo-by-oauth2code
  "for oauth2 redirect"
  [{:keys [appid secret code op-fn]}]
  (get-oauth2-access-token
    {:appid appid :appsecret secret
     :code code
     :op-fn (fn [{:keys [errcode errmsg access_token openid unionid]}]
              (get-oauth2-userinfo
                {:op-fn op-fn
                 :access_token access_token
                 :openid openid}))}))
