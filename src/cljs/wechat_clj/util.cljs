(ns wechat-clj.util
  (:require [cognitect.transit :as t]))

(defn ^:export is-weixin []
  (let [ua (-> js/navigator
             .-userAgent
             .toLowerCase)]
    (not= (.indexOf ua "micromessenger") -1)))

(def json-reader (t/reader :json))

(defn json-string-to-clj [json-stri]
  (t/read json-reader
    (-> json-stri
      (clojure.string/replace "&quot;" "\""))))
