(ns mini-program-cljs.request
  (:require [mini-program-cljs.util :refer [alert]]))

(defn set-header [headers]
  (clj->js
    (merge {"Content-Type" "application/json; charset=UTF-8"}
      (js->clj headers))))

(defn request [url method data header]
  (js/Promise.
    (fn [^js resolve ^js reject]
      (.request js/wx
        #js {:url url
             :method method
             :data data
             :header header
             :success (fn [^js request]
                        (js/console.log request)
                        (resolve request))
             :fail (fn [^js error]
                     (reject error))}))))
