// src/assets/js/AMap.js
/* eslint-disable */
export default function MapLoader () {    
    return new Promise((resolve, reject) => {
      if (window.AMap) {
        resolve(window.AMap)
      } else {
        var script = document.createElement('script')
        script.type = 'text/javascript'
        script.async = true
        // 虽然我也不知道这两个js文件有什么不一样，但是a不可用，b可用
        script.src = 'https://webapi.amap.com/maps?v=1.4.14&key=99912677fa13df31cf34ccd5679db9db'  // a
        script.src = 'https://webapi.amap.com/maps?v=1.4.14&callback=initAMap&key=99912677fa13df31cf34ccd5679db9db' // b 
        script.onerror = reject
        document.head.appendChild(script)
      }
      window.initAMap = () => {
        resolve(window.AMap)
      }
    })
  }