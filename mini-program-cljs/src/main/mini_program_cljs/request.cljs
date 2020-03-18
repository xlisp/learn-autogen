(ns mini-program-cljs.request
  (:require-macros [mini-program-cljs.macro :refer [defn-js call-promise-1]])
  (:require [mini-program-cljs.util :refer [alert] :as u]
            [mini-program-cljs.js-wx :refer [js-wx]]))

(defn set-header [headers]
  (clj->js
    (merge {"Content-Type" "application/json; charset=UTF-8"}
      (js->clj headers))))

(comment
  ;; 在控制台上面有显示出来: 参数和头部都是正确的, 但是没有alert出来结果
  (js-wx "request"
    #js {:url "https://www.test.com/testjson"
         :method "GET"
         :data #js {:aaa 111}
         :header #js {"Content-Type" "application/json; charset=UTF-8"}
         :success (fn [^js req]
                    (js/console.log "request OK:" req)
                    (alert (str req))
                    )
         :fail (fn [^js error]
                 (js/console.log "request Error: " error))})
  ;; 控制台有请求参数aaa但是没有头部信息aaaa
  (call-promise-1
    (fn [res]
      (prn "请求返回:" res))
    (request #js {:url "https://www.test.com/testjson"
                  :method "GET"
                  :data #js {:aaa 222}
                  :headers (set-header {:token "aaaa"})})))
(defn-js request
  [:url :method :data :header]
  (js/console.log url)
  (js/Promise.
    (fn [^js resolve ^js reject]
      (js-wx "request"
        #js {:url url
             :method method
             :data data
             :header header
             :success (fn [^js request]
                        (js/console.log request)
                        (resolve request))
             :fail (fn [^js error]
                     (reject error))}))))
