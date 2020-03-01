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
