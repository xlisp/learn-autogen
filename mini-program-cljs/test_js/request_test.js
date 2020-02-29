var ff = require("mini-program-cljs");

ff.MiniCljs.request("https://www.test.com/testjson", "GET", {}).then(res => {
    console.log("2222")
    console.log(res);
}).catch(err => {
    wx.showToast({
        title: err.message,
        icon: 'none'
    })
})

