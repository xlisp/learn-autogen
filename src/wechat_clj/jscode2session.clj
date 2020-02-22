(ns wechat-clj.jscode2session
  (:require
   [clj-http.client :as client]
   [cheshire.core :as cjson])
  (:import [wechat_clj JavaHelper]))

(defn get-jscode2session
  [{:keys [appid secret jscode op-fn]}]
  (let [api-host "https://api.weixin.qq.com/sns/jscode2session"
        {:keys [errcode errmsg hints session_key expires_in openid unionid] :as res}
        (-> api-host
          (client/get {:query-params
                       {:appid      appid
                        :secret     secret
                        :js_code    jscode
                        :grant_type "authorization_code"}})
          (:body)
          (cjson/parse-string true))]
    (op-fn res)))

(defn decrypt-wxdata
  [{:keys [encrypted-data session-key iv]}]
  (-> (JavaHelper/decrypt encrypted-data session-key iv "UTF-8")
    (cjson/parse-string true)))
