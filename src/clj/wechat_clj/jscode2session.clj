(ns wechat-clj.jscode2session
  (:require
   [clj-http.client :as client]
   [cheshire.core :as cjson]
   [wechat-clj.util :as u]))

;; 注意: 小程序端的登陆代码必须按照下面写法来写,才能同时拿到code, encrypted-data 和 iv 传给的decrypt-wxdata去解析
;; wx.login({
;;     success: function (r) {
;;         console.log('------r-----');
;;         console.log(r);
;;         var code = r.code;//登录凭证
;;         if (code) {
;;             //2、调用获取用户信息接口
;;             wx.getUserInfo({
;;                 success: function (res) {
;;                     console.log({encryptedData: res.encryptedData, iv: res.iv, code: code})
;;                     //3.解密用户信息 获取unionId
;;                     //...
;;                 },
;;                 fail: function () {
;;                     console.log('获取用户信息失败')
;;                 }
;;             })
;;
;;         } else {
;;             console.log('获取用户登录态失败！' + r.errMsg)
;;         }
;;     },
;;     fail: function () {
;;         callback(false)
;;     }
;; })
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
  (-> (u/decrypt encrypted-data session-key iv "UTF-8")
    (cjson/parse-string true)))
