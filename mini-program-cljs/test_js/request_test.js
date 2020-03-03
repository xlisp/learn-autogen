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

ff.MiniCljs.request("https://www.test.com/testpay333", "POST", {'aaa': 12112, 'bbb': 33355}).then(res => {
    console.log("2222")
    console.log(res);
}).catch(err => {
    wx.showToast({
        title: err.message,
        icon: 'none'
    })
})


ff.MiniCljs.request({url: "https://www.test.com/testjson", method: "GET", data: {aaa: 222, bbb: 22}}).then(res => {
    console.log("2222")
    console.log(res);
}).catch(err => {
    wx.showToast({
        title: err.message,
        icon: 'none'
    })
})
