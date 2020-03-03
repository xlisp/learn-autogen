(ns mini-program-cljs.util)

;; TODO: 需要写个宏来生成这样的函数,帮它转好参数为clojure的keys风格
(defn jsx->clj
  [x]
  (into {} (for [k (.keys js/Object x)] [(keyword k) (aget x k)])))

(defn alert [title]
  (.showToast js/wx
    #js {:title title
         :icon "none"
         :mask false
         :duration 3000}))

(defn switch-router [url]
  (.navigateTo js/wx
    #js {:url url}))

(defn set-storage-sync [key values]
  (.setStorageSync js/wx
    #js {:key key
         :values (.stringify js/JSON values)}))

(defn get-storage-sync [key]
  (.parse js/JSON
    (.getStorageSync js/wx key)))
