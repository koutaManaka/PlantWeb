<script setup>
    import {computed, onBeforeUnmount, onMounted, onUnmounted, ref, watchEffect} from 'vue';
    import {ElMessage} from 'element-plus';
    import {useRouter} from 'vue-router';
    import requestOneNet from "@/utils/requestOneNet";

    const router = useRouter()
    const data = ref(1)
    const humidity = ref(1)
    const water = ref(1)
    let waterValue = parseFloat(water.value)
    let ans = waterValue*0.167+1.167
    let interval = null
    const userData = localStorage.getItem("user")
    const user = ref({})
    user.value = JSON.parse(userData)
    if(waterValue < 0.8) {
        ans = 0
    }
    ans = ans.toFixed(2)
    const optionW = computed(()=>{
        return {
            tooltip: {
                formatter: '{a} <br/>{b} : {c}%',
            },
            series: [
                {
                    name: 'WaterLine',
                    type: 'gauge',
                    max: 5,
                    min: 0,
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}',
                        textStyle: {
                            fontSize: 16 // 调整字体大小为16px
                        }
                    },
                    data: [
                        {
                            value: ans,
                            name: '水位cm',
                            fontSize: 9
                        }
                    ]
                }
            ]
        }
    })
    const optionH = computed(()=>{
        return {
            tooltip: {
                formatter: '{a} <br/>{b} : {c}%',
            },
            series: [
                {
                    name: 'Humidity',
                    type: 'gauge',
                    max: 85,
                    min: 0,
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}',
                        textStyle: {
                            fontSize: 16 // 调整字体大小为16px
                        }
                    },
                    data: [
                        {
                            value: humidity.value,
                            name: '湿度%',
                            fontSize: 9
                        }
                    ]
                }
            ]
        }
    })
    const option = computed(() => {
        return {
            tooltip: {
                formatter: '{a} <br/>{b} : {c}%',
            },
            series: [
                {
                    name: 'Temperature',
                    type: 'gauge',
                    max: 65,
                    min: 0,
                    progress: {
                        show: true
                    },
                    detail: {
                        valueAnimation: true,
                        formatter: '{value}',
                        textStyle: {
                            fontSize: 16 // 调整字体大小为16px
                        }
                    },
                    data: [
                        {
                            value: data.value,
                            name: '温度℃',
                            fontSize: 9
                        }
                    ]
                }
            ]
        }
    })

    const getData = async () => {
        try {
            const res = await requestOneNet.get("/devices/datapoints?devIds=1040544350&timestamp=" + Date.now(), {
                headers: {
                    'Content-Type': 'application/json',
                    'api-key': 'GoVrky0s0q0911dhkH57NWue0oE='
                }
            });
            let str = res.data.data.devices[0].datastreams[0].value;
            str = str.split(':');
            humidity.value = str[0];
            data.value = str[1];
            water.value = str[2];
        } catch (err) {
            console.log(err);
        }
    }

    function Exit() {
        router.push("/login")
        localStorage.removeItem("LoginForm");
        localStorage.clear();
        ElMessage.success('Exit Successfully');
    }

    const setTimer = () => {
        interval = setInterval(getData,20000)
    }

    onMounted(()=>{
        getData()
        setTimer()
    })

    onBeforeUnmount(() => {
        clearInterval(interval)
        interval = null
    })

</script>

<template>
    <el-container class="layout-container-demo" style="height: 100%">
        <el-aside width="210px" style="height: 100%;background-color:rgb(48,65,86)">
            <el-scrollbar>
                <el-menu :default-openeds="['1','2']" style="height: 100%; overflow-x: hidden;"
                         background-color="rgb(48,65,86)" text-color="#fff" active-text-color="#ffd04b">
                    <div style="height: 60px; line-height: 60px; text-align:center">
                        <b>智慧种植系统</b>
                    </div>
                    <el-sub-menu index="1">
                        <template #title>
                            <el-icon>
                                <search/>
                            </el-icon>
                            查看设备
                        </template>
                        <el-menu-item-group title="数据显示">
                            <el-menu-item index="1-1">气体数据</el-menu-item>
                            <el-menu-item index="1-2">液体数据</el-menu-item>
                            <el-menu-item index="1-3">环境数据</el-menu-item>
                        </el-menu-item-group>
                        <el-menu-item-group title="远程操作">
                            <el-menu-item index="1-4">气体操作</el-menu-item>
                            <el-menu-item index="1-5">液体操作</el-menu-item>
                            <el-menu-item index="1-6">环境操作</el-menu-item>
                        </el-menu-item-group>
                        <el-sub-menu index="1-7">
                            <template #title>历史统计</template>
                            <el-menu-item index="1-7-1">设备历史数据</el-menu-item>
                            <el-menu-item index="1-7-2">设备日志</el-menu-item>
                            <el-menu-item index="1-7-3">有关种植</el-menu-item>
                        </el-sub-menu>
                    </el-sub-menu>
                    <el-sub-menu index="2">
                        <template #title>
                            <el-icon>
                                <setting/>
                            </el-icon>
                            设备管理
                        </template>
                        <el-menu-item-group>
                            <template #title>修改/删除设备</template>
                            <el-menu-item index="2-1">修改设备</el-menu-item>
                            <el-menu-item index="2-2">删除设备</el-menu-item>
                        </el-menu-item-group>
                        <el-menu-item-group title="新增设备">
                            <el-menu-item index="2-3">添加设备信息</el-menu-item>
                        </el-menu-item-group>
                    </el-sub-menu>
                    <el-sub-menu index="3">
                        <template #title>
                            <el-icon>
                                <message/>
                            </el-icon>
                            消息通知
                        </template>
                        <el-menu-item-group>
                            <template #title>植株相关</template>
                            <el-menu-item index="3-1">植株生长周报</el-menu-item>
                        </el-menu-item-group>
                        <el-menu-item-group title="设备/版本相关">
                            <el-menu-item index="3-2">设备消息</el-menu-item>
                            <el-menu-item index="3-3">版本更新</el-menu-item>
                        </el-menu-item-group>
                    </el-sub-menu>
                </el-menu>
            </el-scrollbar>
        </el-aside>

        <el-container>
            <el-header style="text-align: right; font-size: 12px; border-bottom: 1px solid #ccc">
                <div class="toolbar">
                    <el-dropdown>
                        <el-icon style="margin-right: 8px; margin-top: 1px">
                            <ArrowDownBold/>
                        </el-icon>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <router-view></router-view>
                                <el-dropdown-item>
                                    <router-link to="/personal">个人主页</router-link>
                                </el-dropdown-item>
                                <el-dropdown-item><span style="text-decoration-line: none"
                                                        @click="Exit()">退出登录</span></el-dropdown-item>
                                <el-dropdown-item>
                                    <router-link to="/help">帮助</router-link>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                    <span>{{user.username}}</span>
                </div>
            </el-header>
            <el-main>
                <el-row type="flex" class="first">
                    <el-col :span="7" class="box">
                        <e-charts class="chart" :option="option"></e-charts>
                    </el-col>
                    <el-col :span="7" class="box">
                        <e-charts class="chart" :option="optionH"></e-charts>
                    </el-col>
                    <el-col :span="7" class="box" style="margin-right: 15px">
                        <e-charts class="chart" :option="optionW"></e-charts>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </el-container>
</template>

<style scoped>
    .layout-container-demo .el-header {
        position: relative;
    }

    .layout-container-demo .el-aside {
        color: rgb(255, 255, 255);
    }

    .layout-container-demo .el-menu {
        border-right: none;
    }

    .layout-container-demo .el-main {
        padding: 0;
    }

    .layout-container-demo .toolbar {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        right: 20px;
    }

    a {
        text-decoration: none;
        color: #606266;
    }

    .router-link-active {
        text-decoration: none;
    }

    .first {
        margin-top: 50px;
        justify-content: space-between;
    }

    .box {
        height: 320px;
        background-color: white;
        margin-left: 10px;
    }

    .img {
        width: 100px;
    }
</style>