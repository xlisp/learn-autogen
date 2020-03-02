(ns mini-program-cljs.login
  (:require [mini-program-cljs.util :refer [alert]]))

;; 微信官方接口的改变: 这个接口只能在开发环境,并且要点了用户授权button/getUserInfo之后才能用
(defn get-user-info [success-fn fail-fn]
  (.getUserInfo js/wx
    #js {:success success-fn
         :fail-fn fail-fn}))

(defn login [success-fn iv encrypted-data]
  (.login js/wx
    #js {:success
         (fn [^js r]
           (let [code (.-code r)]
             (if (empty? code)
               (alert (str "登录失败!" (.-errMsg r)))
               (success-fn #js {:encryptedData encrypted-data
                                :iv iv
                                :code code}))
             ))
         :fail (fn []
                 (alert (str "登录失败!")))}))
