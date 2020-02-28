(defproject wechat-clj "0.1.8-SNAPSHOT"
  :description "微信支付,网络授权,加密库: 包括了前端的wx签名等"
  :url "https://clojars.org/wechat-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"]
                 [cheshire "5.10.0"]
                 [com.github.wxpay/wxpay-sdk "0.0.3"]
                 [clj-http "3.10.0"]
                 [org.clojure/core.async "0.7.559"]
                 [com.cognitect/transit-clj "1.0.324"]]

  :plugins [[lein-doo "0.1.11"]
            [lein-cljsbuild "1.1.7"]]

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :java-source-paths ["src/java"]

  :cljsbuild
  {:builds {:minify {:source-paths ["src/cljs"]
                     :compiler {:optimizations :advanced
                                :pretty-print false}}
            :dev {:source-paths ["src/cljs"]
                  :compiler {:optimizations :whitespace}}
            :test {:id "test"
                   :source-paths ["src/cljs" "test"]
                   :compiler {:output-to "target/cljs-tests.js"
                              :output-dir "target"
                              :main wechat-clj.core
                              :optimizations :none
                              :target :nodejs}}}}

  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :repl-options {:init-ns wechat-clj.core})
