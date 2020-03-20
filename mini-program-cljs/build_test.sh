#!/bin/bash
## TODO: 需要编译过了才能npm push,所以需要make-process需要filter跟踪
ruby -p -i -e 'gsub("[miniprogram-automator :as automator]", "")' src/main/mini_program_cljs/js_wx.cljs
yarn release
git checkout src/main/mini_program_cljs/js_wx.cljs
