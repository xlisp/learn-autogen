(ns mini-program-cljs.util)

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
