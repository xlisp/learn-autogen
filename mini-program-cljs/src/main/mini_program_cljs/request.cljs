(ns mini-program-cljs.request
  (:require [mini-program-cljs.util :refer [alert] :as u]
            [mini-program-cljs.js-wx :refer [js-wx]]))

(defn set-header [headers]
  (clj->js
    (merge {"Content-Type" "application/json; charset=UTF-8"}
      (js->clj headers))))

(defn request [^js options]
  (let [{:keys [url method data header]}
        (u/jsx->clj options)]
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
                       (reject error))})))))
