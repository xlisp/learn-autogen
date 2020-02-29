(ns mini-program-cljs.login
  (:require [mini-program-cljs.util :refer [alert]]))

(defn get-user-info [success-fn fail-fn]
  (.getUserInfo js/wx
    #js {:success success-fn
         :fail-fn fail-fn}))

(defn login [success-fn]
  (.login js/wx
    #js {:success
         (fn [^js r]
           (let [code (.-code r)]
             (if (empty? code)
               (alert (str "登录失败!" (.-errMsg r)))
               (get-user-info
                 (fn [^js res]
                   (success-fn #js {:encryptedData (.-encryptedData res)
                                    :iv (.-iv res)
                                    :code code}))
                 (fn [] (alert "获取用户信息失败!"))))
             ))
         :fail (fn [] false)}))
