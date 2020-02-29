(ns mini-program-cljs.util)

(defn alert [title]
  (.showToast js/wx
    #js {:title title
         :icon "none"
         :mask false
         :duration 3000}))
