import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import elementIcon from "./plugins/icons";
import './assets/gloable.css'
import router from './router'
import  ECharts from 'vue-echarts'
import 'echarts'
import request from "@/utils/request";
import requestOneNet from "@/utils/requestOneNet";

const app = createApp(App)

app.config.globalProperties.request=request
app.config.globalProperties.requestOneNet=requestOneNet


app.use(elementIcon)
app.component('ECharts',ECharts)
app.use(ElementPlus).use(router)
app.mount('#app')

