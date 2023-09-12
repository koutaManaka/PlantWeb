<script setup>
    import {computed, onMounted, ref} from 'vue';
    import {ElMessage} from 'element-plus';
    import {useRouter} from 'vue-router';
    import requestOneNet from "@/utils/requestOneNet";

    const router = useRouter();
    const data = ref(1);
    const option = computed(() => {
        return {
            series: [
                {
                    type: 'gauge',
                    center: ['50%', '60%'],
                    startAngle: 200,
                    endAngle: -20,
                    min: 0,
                    max: 60,
                    splitNumber: 12,
                    itemStyle: {
                        color: '#FFAB91'
                    },
                    progress: {
                        show: true,
                        width: 30
                    },
                    pointer: {
                        show: false
                    },
                    axisLine: {
                        lineStyle: {
                            width: 30
                        }
                    },
                    axisTick: {
                        distance: -45,
                        splitNumber: 5,
                        lineStyle: {
                            width: 2,
                            color: '#999'
                        }
                    },
                    splitLine: {
                        distance: -52,
                        length: 14,
                        lineStyle: {
                            width: 3,
                            color: '#999'
                        }
                    },
                    axisLabel: {
                        distance: -20,
                        color: '#999',
                        fontSize: 20
                    },
                    anchor: {
                        show: false
                    },
                    title: {
                        show: false
                    },
                    detail: {
                        valueAnimation: true,
                        width: '60%',
                        lineHeight: 40,
                        borderRadius: 8,
                        offsetCenter: [0, '-15%'],
                        fontSize: 40,
                        fontWeight: 'bolder',
                        formatter: '{value} °C',
                        color: 'inherit'
                    },
                    data: [
                        {
                            value: data.value
                        }
                    ]
                },
                {
                    type: 'gauge',
                    center: ['50%', '60%'],
                    startAngle: 200,
                    endAngle: -20,
                    min: 0,
                    max: 60,
                    itemStyle: {
                        color: '#FD7347'
                    },
                    progress: {
                        show: true,
                        width: 8
                    },
                    pointer: {
                        show: false
                    },
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    detail: {
                        show: false
                    },
                    data: [
                        {
                            value: data.value
                        }
                    ]
                }
            ]
        }
    })

    async function GetData() {
        try {
            const res = await requestOneNet.get("/devices/datapoints?devIds=1040544350", {
                headers: {
                    'Content-Type': 'application/json',
                    'api-key': 'GoVrky0s0q0911dhkH57NWue0oE='
                }
            });
            let str = res.data.data.devices[0].datastreams[0].value;
            str = str.split(':');
            data.value = str[1];
        } catch (err) {
            console.log(err);
        }
    }

    onMounted(() => {
        GetData();
        setInterval(GetData, 50000);
    });

    function Exit() {
        router.push("/login")
        localStorage.removeItem("LoginForm");
        localStorage.clear();
        ElMessage.success('Exit Successfully');
    }
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
                    <!--                    <span>{{ LoginForm.username }}</span>-->
                </div>
            </el-header>
            <el-main>
                <el-row type="flex" class="first">
                    <el-col :span="7" class="box">
                        <div>温度</div>
                        <e-charts class="chart" :option="option"></e-charts>
                    </el-col>
                    <el-col :span="7" class="box"></el-col>
                    <el-col :span="7" class="box" style="margin-right: 15px"></el-col>
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