import axios from 'axios'

// create an axios instance
const requestOneNet = axios.create({
    baseURL: '/onenet',
    timeout: 50000
})
// requestOneNet.base = '/onenet'
// request interceptor
// requestOneNet.interceptors.request.use(
//     config => {
//         // do something before request is sent
//
//         // config.headers['token'] = getToken()
//         return config
//     },
//     error => {
//         // do something with request error
//         console.log(error) // for debug
//         return Promise.reject(error)
//     }
// )

// response interceptor
// requestOneNet.interceptors.response.use(
//     response => {
//         const res = response.data
//
//         // if the custom code is not 20000, it is judged as an error.
//         // 这里可根据实际情况，自行对响应进行拦截处理
//         if (res.code !== 200) {
//             // console.log(res.code)
//             return Promise.reject(new Error(res.message || 'Error'))
//         } else {
//             return res
//         }
//     },
//     error => {
//         console.log('err' + error) // for debug
//         return Promise.reject(error)
//     }
// )

export default requestOneNet