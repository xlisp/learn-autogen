## [微信小程序的cljs库mini-program-cljs](https://www.npmjs.com/package/mini-program-cljs)

```sh

npm i mini-program-cljs

```

``` sh


➜  test-path npm i  mini-program-cljs
+ mini-program-cljs@0.1.1
added 1 package in 1.157s

### 主命名空间(package.json => main)

➜  test-path node
> var x = require("mini-program-cljs");
undefined
> x.
x.__defineGetter__      x.__defineSetter__      x.__lookupGetter__
x.__lookupSetter__      x.__proto__             x.constructor
x.hasOwnProperty        x.isPrototypeOf         x.propertyIsEnumerable
x.toLocaleString        x.toString              x.valueOf

> x.version(1)
'0.1.1, 1!'

### 其他的命名空间
>
> var y = require("mini-program-cljs/target/release/mini_program_cljs.request");
undefined
> y.request
[Function: request]
> y.request({})
ReferenceError: wx is not defined
    at Object.request (/Users/clojure/WeChatProjects/test-path222/node_modules/mini-prog
ram-cljs/target/release/mini_program_cljs.request.js:2:37)
>

```
