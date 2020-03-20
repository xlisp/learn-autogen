(ns mini-program-cljs.components.timer)

(comment
  (timer-component 10 @mini-program-cljs.js-wx/current-page))
(defn timer-component [time self]
  (do
    (.setData self #js {:timeNumber time
                        :timerIsStop nil})
    (let [timer (atom time)]
      (js/setInterval
        (fn []
          (if (> @timer 0)
            (do
              (swap! timer dec)
              (if (> @timer 0)
                (do (js/console.log "倒计时:" @timer)
                    (.setData self #js {:timeNumber @timer}))

                (.setData self #js {:timerIsStop true})))
            nil))
        1000))))
