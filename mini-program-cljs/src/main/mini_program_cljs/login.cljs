(ns mini-program-cljs.login
  (:require-macros [mini-program-cljs.macro
                    :refer [defn-js call-promise-1]])
  (:require [mini-program-cljs.util :refer [alert log] :as u]
            [mini-program-cljs.js-wx :refer [js-wx] :as jswx]))

;; 微信官方接口的改变: 这个接口只能在开发环境,并且要点了用户授权button/getUserInfo之后才能用

(comment
  (def aaa (atom ""))

  (log @aaa)

  (call-promise-1
    (fn [res]
      (reset!  aaa res)
      (alert (str "---" (.-rawData res))))
    (get-user-info #js {:failFn #(alert (str %))
                        :successFn #(alert (str %))})))
(defn-js get-user-info
  [:successFn :failFn]
  jswx/export-js
  (js-wx "getUserInfo"
    #js {:success successFn
         :fail failFn}))

(comment
  ;; 打印出来code的内容了
  (call-promise-1
    (fn [res]
      (alert (str "---" (.-code res))))
    (login #js {:successFn #(alert (str %))
                :iv "aaa" :encryptedData "bbb" })))
(defn-js login
  [:successFn :iv :encryptedData]
  jswx/export-js
  (js-wx "login"
    #js {:success
         (fn [^js r]
           (let [code (.-code r)]
             (if (empty? code)
               (alert (str "登录失败!" (.-errMsg r)))
               (successFn #js {:encryptedData encryptedData
                               :iv iv
                               :code code}))))
         :fail (fn []
                 (alert (str "登录失败!")))}))
