(ns wechat-clj.oauth2
  (:require [clj-http.client :as client]
            [cheshire.core :as cjson]))

(defn parse-query-string [query-string]
  (if (empty? query-string)
    {}
    (let [params
          (-> query-string
            (clojure.string/split #"=|&"))]
      (->> params
        (partition 2)
        (map (fn [item] [(keyword (first item))
                         (java.net.URLDecoder/decode (last item) "UTF-8")]))
        (into {})))))

(comment
  (parse-query-string "")
  ;; => {}
  (parse-query-string "aaa=bbb&ccc=ddd")
  ;; => {:aaa "bbb", :ccc "ddd"}
  )

(defn generate-oauth2-url
  [{:keys [url appid]}]
  (let [encode-url (java.net.URLEncoder/encode url)
        oauth2-url
        (fn [url]
          (str "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
            appid  "&redirect_uri=" url
            "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"))]
    (oauth2-url encode-url)))

;; 一个code只能换取一次oauth2-access-token
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

(comment
  ;; get-oauth2-access-token token-res:
  {:access_token "dasdsadsadsa", :expires_in 7200, :refresh_token "dasdasdas", :openid "opb8V1Q7oNvSdasdsa2NR7JA", :scope "snsapi_userinfo", :unionid "oJ9ML0gGvR1i5Qvdsadsadasdas"}
  )

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
  "通过code一次性获得用户的信息"
  [{:keys [appid secret code op-fn]}]
  (get-oauth2-access-token
    {:appid appid :appsecret secret
     :code code
     :op-fn (fn [{:keys [errcode errmsg access_token openid unionid]}]
              (get-oauth2-userinfo
                {:op-fn op-fn
                 :access_token access_token
                 :openid openid}))}))

(comment
  (get-wxuserinfo-by-oauth2code
    {:code code
     :op-fn (fn [{:keys [openid] :as wxuser}]
              ;; ...
              )}))
