#!/bin/bash
# Successfully compiled ["/Users/clojure/CljPro/wechat-clj/target/cljsbuild-main.js"] in 12.255 seconds.
# Successfully compiled ["target/cljs-tests.js"] in 0.622 seconds.
lein cljsbuild once
lein deploy clojars
