import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import elementIcon from "./plugins/icons";
import './assets/gloable.css'
import router from './router'
import  ECharts from 'vue-echarts'
import 'echarts'
import locale from 'element-plus/lib/locale/lang/zh-cn'
import request from "@/utils/request";
import requestOneNet from "@/utils/requestOneNet";
import dataV from '@jiaminghi/data-view'
import * as ElIconModules from './plugins/icons'

const app = createApp(App)

app.config.globalProperties.request=request
app.config.globalProperties.requestOneNet=requestOneNet

app.use(elementIcon)
app.component('ECharts',ECharts)
app.use(ElementPlus,{locale}).use(router)
app.use(dataV)
app.mount('#app')
for (const iconName in ElIconModules) {
    app.component(iconName, ElIconModules[iconName])
}

