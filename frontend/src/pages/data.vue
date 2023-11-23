<script setup>
    import {computed, nextTick, onBeforeUnmount, onMounted, onUnmounted, ref} from 'vue';
    import requestOneNet from "@/utils/requestOneNet";

    document.onkeydown = () => {

        //禁用F12
        if (window.event && window.event.keyCode == 123) {
            return false;
            //禁用ctrl+shift+i,
        } else if (window.event.ctrlKey && window.event.shiftKey && window.event.keyCode == 73) {
            return false;
            //屏蔽Shift+F10
        } else if (window.event.shiftKey && window.event.keyCode == 121) {
            return false;
        }
    };

    const userData = localStorage.getItem("user")
    const user = ref({})
    const chatVisible = ref(true)
    user.value = JSON.parse(userData)
    const temperature = ref(1)
    const humidity = ref(1)
    const water = ref(1)
    const videoContainer = ref(null);
    let videoStream = null;
    const waterValue = ref(parseFloat(water.value));
    const humidityValue = ref(parseFloat(humidity.value));
    const temperatureValue = ref(parseFloat(temperature.value));
    let ans = waterValue.value * 0.167 + 1.167 < 0.8 ? 0 : waterValue.value * 0.167 + 1.167
    ans = ans.toFixed(2)
    let chartInterval = null
    let alarmInterval = null
    const H_option = computed(() => {
        return {
            series: [
                {
                    type: 'gauge',
                    startAngle: -Math.PI / 2,
                    endAngle: Math.PI * 1.5,
                    arcLineWidth: 25,
                    data: [
                        {name: 'itemA', value: humidity.value, gradient: ['#03c2fd', '#1ed3e5', '#2fded6']}
                    ],
                    axisLabel: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    pointer: {
                        show: false
                    },
                    dataItemStyle: {
                        lineCap: 'round'
                    },
                    details: {
                        show: true,
                        formatter: '湿度' + humidity.value + '%',
                        style: {
                            fill: '#1ed3e5',
                            fontSize: 30
                        }
                    }
                }
            ]
        }
    })

    const T_option = computed(() => {
        return {
            title: {
                text: '温度计',
                style: {
                    fill: '#ffffff',
                    fontSize: 25
                }
            },
            series: [
                {
                    type: 'gauge',
                    data: [{name: 'itemA', value: temperature.value, gradient: ['#e7bcf3', '#e690d1', '#fb7293']}],
                    axisLabel: {
                        formatter: '{value}℃',
                        style: {
                            fill: '#ffffff'
                        }
                    },
                    details: {
                        show: true,
                        offset: [3, 20],
                        formatter: temperature.value + '℃',
                        style: {
                            fill: '#ffffff'
                        }
                    },
                    animationCurve: 'easeOutBack',
                    min: 5,
                    max: 65
                }
            ]
        }
    })
    const G_config = computed(() => {
        return {
            data: [
                {
                    name: '水位',
                    value: ans,
                },
                {
                    name: '参考水位',
                    value: 8
                }
            ],
            colors: ['#96bfff'],
            unit: 'cm',
        }
    })

    const C_option = computed(() => {
        return {
            title: {
                text: '近两分钟数据趋势',
                textStyle: {
                    color: 'white' // Set the text color for the title
                }
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['Humidity', 'WaterLevel', 'Temperature'],
                textStyle: {
                    color: 'white' // Set the text color for the title
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true,
                textStyle: {
                    color: 'white' // Set the text color for the title
                }
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data:[],
                axisLabel: {
                    textStyle: {
                        color: 'white' // Set the text color for the xAxis labels
                    }
                }

            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    textStyle: {
                        color: 'white' // Set the text color for the yAxis labels
                    }
                }
            },
            series: [
                {
                    name: 'Humidity',
                    type: 'line',
                    stack: 'Total1',
                    data:[]
                },
                {
                    name: 'WaterLevel',
                    type: 'line',
                    stack: 'Total2',
                    data:[]
                },
                {
                    name: 'Temperature',
                    type: 'line',
                    stack: 'Total3',
                    data:[]
                }
            ],
        }
    })


    const getDataAndDrawChart = async () => {
        try {
            const res = await requestOneNet.get("/devices/datapoints", {
                headers: {
                    'Content-Type': 'application/json',
                    'api-key': user.value.apiKey
                },
                params: {
                    devIds: user.value.deviceId,
                    timestamp: Date.now()
                }
            });
            let str = res.data.data.devices[0].datastreams[0].value;
            str = str.split(':');
            humidity.value = str[0];
            temperature.value = str[1];
            water.value = str[2];
            waterValue.value = parseFloat(water.value)
            humidityValue.value = parseFloat(humidity.value)
            temperatureValue.value = parseFloat(temperature.value)
            ans = waterValue.value * 0.167 + 1.167 < 0.8 ? 0 : waterValue.value * 0.167 + 1.167
            ans = ans.toFixed(2);
            // 更新折线图数据
            const newData = {
                humidity: parseFloat(humidity.value),
                water: parseFloat(ans),
                temperature: parseFloat(temperature.value),
                time: new Date().toLocaleTimeString() // 使用当前时间作为时间戳
            };
            updateChart(newData);
        } catch (err) {
            console.log(err);
        }
    }

    const updateChart = (newData) =>{
        // 将新数据添加到折线图的数据数组中
        const option = C_option.value
        option.xAxis.data.push(newData.time);
        option.series[0].data.push(newData.humidity);
        option.series[1].data.push(newData.water);
        option.series[2].data.push(newData.temperature);
        // 限制数据数组的长度为最近两分钟的数据
        const maxDataLength = 120; // 假设每秒获取一条数据，最近两分钟即为120条数据
        if (option.xAxis.data.length > maxDataLength) {
            option.xAxis.data.shift(); // 删除数组开头的数据
            option.series[0].data.shift();
            option.series[1].data.shift();
            option.series[2].data.shift();
        }
        chatVisible.value = false;
        nextTick(()=>{
            chatVisible.value = true
        });
    }


    const t_requestBody_1 = {
        cold: "01"
    }

    const t_requestBody_2 = {
        hot: "02"
    }

    const h_requestBody_1 = {
        dry: "03"
    }

    const h_requestBody_2 = {
        wet: "04"
    }

    const Alarm = () => {
        if(temperatureValue.value < 15 || temperatureValue.value > 35) {
            if(temperatureValue.value < 15) {
                try {
                    const res = requestOneNet.post("/cmds", t_requestBody_1,{
                        headers: {
                            'Content-Type': 'text/plain',
                            'api-key': user.value.apiKey
                        },
                        params: {
                            device_id: user.value.deviceId
                        }
                    })
                } catch (error) {
                    console.log(error)
                }
            } else if(temperatureValue.value > 35){
                try {
                    const res = requestOneNet.post("/cmds", t_requestBody_2,{
                        headers: {
                            'Content-Type': 'text/plain',
                            'api-key': user.value.apiKey
                        },
                        params: {
                            device_id: user.value.deviceId
                        }
                    })
                } catch (error) {
                    console.log(error)
                }
            }
        }
        if(humidityValue.value < 20 || humidityValue.value >80) {
            if(humidityValue.value < 20) {
                try {
                    const res = requestOneNet.post("/cmds", h_requestBody_1,{
                        headers: {
                            'Content-Type': 'text/plain',
                            'api-key': user.value.apiKey
                        },
                        params: {
                            device_id: user.value.deviceId
                        }
                    })
                } catch (error) {
                    console.log(error)
                }
            } else if(humidityValue.value > 80) {
                try {
                    const res = requestOneNet.post("/cmds", h_requestBody_2,{
                        headers: {
                            'Content-Type': 'text/plain',
                            'api-key': user.value.apiKey
                        },
                        params: {
                            device_id: user.value.deviceId
                        }
                    })
                } catch (error) {
                    console.log(error)
                }
            }
        }
    }

    const setTimer = () => {
        chartInterval = setInterval(getDataAndDrawChart, 20000);
        alarmInterval = setInterval(Alarm,20000)
    }

    onMounted(() => {
        getDataAndDrawChart()
        // Alarm()
        setTimer()
        if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({ video: true })
                .then((stream) => {
                    videoStream = stream;
                    const videoElement = document.createElement('video');
                    videoElement.srcObject = stream;
                    videoElement.style.width = '100%';
                    videoElement.style.height = '100%';
                    videoElement.autoplay = true;
                    videoContainer.value.appendChild(videoElement);
                })
                .catch((error) => {
                    console.error('获取摄像头视频流失败：', error);
                });
        } else {
            console.error('浏览器不支持getUserMedia API');
        }
    })

    onUnmounted(() => {
        if (videoStream) {
            videoStream.getTracks().forEach((track) => track.stop());
        }
    });

    onBeforeUnmount(() => {
        clearInterval(chartInterval)
        clearInterval(alarmInterval)
        chartInterval = null
        alarmInterval = null
    })

</script>

<template>
    <dv-full-screen-container class="body">
        <div class="module-box">
            <dv-decoration-6 style="width:250px;height:85px; margin-top:10px; margin-left: 60px;" />
            <dv-border-box-10 class="head">智慧种植系统</dv-border-box-10>
            <dv-decoration-11 style="width:200px;height:70px;margin-right: 20px" class="user">{{user.username}}</dv-decoration-11>
        </div>
        <div class="module-box" style="width: 100%">
            <div style="flex: 1">
                <dv-charts :option="H_option" style="height: 350px;width: 350px;margin-left: 20px"/>
                <dv-charts :option="T_option" style="height: 350px;width: 350px;margin-left: 20px"/>
            </div>
            <div style="flex: 1">
                <dv-border-box-11 title="视频监控" style="min-width:300px;width:495px;height: 400px;margin-top: 10px;margin-left: 40px" class="video">
                    <div ref="videoContainer" style="width:100%;height:85%;margin-top: 52px"></div>
                </dv-border-box-11>
                <div v-if="chatVisible" style="margin-top: 20px;margin-right: 25px">
                    <e-charts class="chart" :option="C_option"></e-charts>
                </div>
            </div>
            <div style="flex: 1">
                <dv-border-box-1 style="width: 500px;margin-left: 75px;margin-right: 15px;height: 400px" class="event" >
                    <h3>检测事件</h3>
                    <h4 style="margin-top: 30px">
                        {{ temperatureValue < 15 ? '注意！温度过低' : temperatureValue > 30 ? '注意！温度过高' : '温度正常' }}
                    </h4>
                    <h4>
                        {{ humidityValue < 20 ? '注意！过于干燥' : humidityValue > 80 ? '注意！过于潮湿' : '湿度正常' }}
                    </h4>
                    <h4>
                        {{ waterValue > 8 ? '注意！水位过高' : '水位正常' }}
                    </h4>
                </dv-border-box-1>
                <dv-border-box-12 style="width:350px;height:110px;margin-left:170px;margin-top: 120px;padding-top: 15px">
                    <dv-capsule-chart :config="G_config" style="width:300px;height:90px;margin-left: 20px"/>
                </dv-border-box-12>
            </div>
        </div>
    </dv-full-screen-container>
</template>

<style scoped>
    * {
        padding: 0;
        margin: 0;
    }

    .chart {
        height: 300px;
    }

    .body {
        width: 100%;
        height: 100vh;
        /* 背景渐变 */
        background-image: linear-gradient(-225deg, #000022 0%, #000044 50%, #000022 100%);
    }
    h4 {
        width: 80%;
        margin: 10px auto;
        border: 4px solid #ccc;
        padding: 5px;
        border-radius: 5px;
        color: #ffffff;
    }
    .head {
        height: 100px;
        width: 550px;
        color: white;
        text-align: center;
        line-height: 90px;
        font-size: 45px;
        font-weight: bolder;
        margin-right: 165px;
    }
    .user {
        color: white;
        text-align: center;
        line-height: 60px;
        font-size: 25px;
        font-weight: bolder;
    }
    .event {
        color: white;
        text-align: center;
        line-height: 60px;
        font-size: 25px;
        font-weight: bolder;
    }
    .module-box {
        margin-top: 20px;
        display: flex;
        justify-content: space-between;
    }
    .video {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>