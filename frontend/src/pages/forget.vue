<script setup >
    import {onMounted, reactive, ref} from 'vue'
    import { ElMessage } from 'element-plus';
    import { useRouter } from 'vue-router';
    import request from "../utils/request.js";

    const Form = reactive({
        code: '',
        password: '',
        passwordTest: '',
        email: ''
    })

    const rules = reactive({
        // code: [
        //     {required: true, message: "请输入验证码", trigger: "blur"},
        //     {min: 4, max: 4, message: '长度在 4 个字符', trigger: 'blur'},
        // ],
        password: [
            {required: true, message: "请输入密码", trigger: "blur"},
            {min: 6, message: '密码长度要大于6', trigger: 'blur'},
        ],
        passwordTest: [{required: true, message: "请再次输入密码", trigger: "blur"}, {
            min: 6,
            message: '密码长度要大于6',
            trigger: 'blur'
        }, {
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== Form.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }],
        email: [
            {required: true, message: "请输入邮箱", trigger: "blur"},
            {
                validator: (rule, value, callback) => {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (emailRegex.test(value)) {
                        callback();
                    } else {
                        callback(new Error('邮箱格式错误'));
                    }
                },
                trigger: 'blur'
            }
        ]
    })


    const buttonLabel = ref('点击发送');

    let timer = null;

    const countdown = ref(60);

    const FormRef = ref();

    const router = useRouter();

    const start = () => {
        localStorage.clear();
    };

    const jump = () => {
        router.push("/login")
    }

    const submit = () => {
        FormRef.value.validate((valid) => {
            if (valid) {
                request.post("/user/change.do", Form).then(res => {
                    if (res.code === '200') {
                        router.push("/login");
                        ElMessage.success("Change Successfully");
                    } else {
                        ElMessage.error(res.message);
                    }
                });
            }
        });
    };

    const disableButton = ref(false);

    const sent = () => {
        disableButton.value = true;
        countdown.value = 60;
        buttonLabel.value = `${countdown.value}s后重试`;

        const countdownPromise = new Promise((resolve, reject) => {
            let timer = setInterval(() => {
                countdown.value--;
                if (countdown.value === 0) {
                    clearInterval(timer);
                    resolve();
                } else {
                    buttonLabel.value = `${countdown.value}s后重试`;
                }
            }, 1000);
        });

        const requestPromise = request.post("/user/sendMsg.do", Form)
            .then(res => {
                if (res.code === '200') {
                    ElMessage.success("Send Successfully");
                } else {
                    ElMessage.error(res.message);
                }
            })
            .catch(error => {
                ElMessage.error("Request Error");
            });

        Promise.all([countdownPromise, requestPromise])
            .finally(() => {
                disableButton.value = false;
                buttonLabel.value = '点击发送';
            });
    };

    onMounted(() => {
        start();
    });

</script>

<template>
    <div class="building">
        <div class="flex">
            <div class="picture">
                <h2 class="font_1">更改密码</h2>
                <el-button type="primary" round class="regiBtn" @click="jump">已有账户?点击登录</el-button>
            </div>
            <div class="my-login">
                <div class="font_2">CHANGE</div>
                <div class="line"></div>
                <el-form :model="Form" :rules="rules" ref="FormRef" label-width="55px" class="top">
                    <el-form-item label="" prop="email">
                        <el-row justify="center">
                            <el-col :span="2">
                                <el-icon class="lock" color="#4E655D">
                                    <Message />
                                </el-icon>
                            </el-col>
                            <el-col :span="21">
                                <el-input class="user" placeholder="请输入邮箱" v-model="Form.email">
                                </el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item label="" prop="password">
                        <el-row justify="center">
                            <el-col :span="2">
                                <el-icon class="lock" color="#4E655D">
                                    <lock/>
                                </el-icon>
                            </el-col>
                            <el-col :span="21">
                                <el-input class="user" placeholder="请输入新的密码" type=“password”
                                          v-model="Form.password"
                                          show-password>
                                </el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item label="" prop="passwordTest">
                        <el-row justify="center">
                            <el-col :span="2">
                                <el-icon class="lock" color="#4E655D">
                                    <lock/>
                                </el-icon>
                            </el-col>
                            <el-col :span="21">
                                <el-input class="user" placeholder="请再次输入密码" type=“password”
                                          v-model="Form.passwordTest" show-password>
                                </el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item label="" prop="code">
                        <el-row justify="center" >
                            <el-col :span="2" style="margin-left: 1px">
                                <el-icon color="#4E655D">
                                    <ChatDotRound />
                                </el-icon>
                            </el-col>
                            <el-col :span="12">
                                <el-input class="user" placeholder="请输入验证码" v-model="Form.code">
                                </el-input>
                            </el-col>
                            <el-col :span="9" class="button-col">
                                <el-button size="small" round style="background-color: #257b5e; color: white;border: #257b5e" @click="sent" :disabled="disabaleButton">{{ buttonLabel }}</el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <el-form-item style="margin-top: 55px">
                        <el-button type="primary" round class="submitBtn" @click="submit">提交</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .building {
        background: url("../assets/backround.png");
        width: 100%;
        height: 100%;
        background-position: center center;
        background-size: cover;
        position: relative;
        position: fixed;
        background-size: 100% 100%;
    }

    .button-col {
        display: flex;
        justify-content: flex-start;
        margin:auto;

    }

    .picture {
        width: 450px;
        height: 500px;
        background: url("../assets/welcome.png");
        background-position: center center;
        background-size: cover;
    }

    .top {
        margin-top: 50px;
    }

    .line {
        height: 2px;
        background: #257b5e;
        width: 80%;
        margin: 0 auto;
    }

    .flex {
        display:flex;
        position:absolute;
        width:800px;
        height:500px;
        top:45%;
        left:50%;
        transform:translate(-50%,-50%);
        box-shadow: 0 12px 16px 0  rgba(0,0,0,0.24), 0 17px 50px 0 #4E655D;
    }

    .my-login {
        width: 350px;
        height: 500px;
        opacity: 1;
        box-shadow: 0 0 25px #909399;
        background-color: rgba(188, 245, 153, 0.3);
    }

    a {
        text-decoration: none;
    }

    .router-link-active {
        text-decoration: none;
    }

    .regiBtn {
        border: solid white 1px;
        background-color: #257b5e ;
        margin-top: 50px;
    }

    .user input {
        border: none;
        color: #fff;
        background-color: transparent;
        font-size: 12px;
    }

    .submitBtn {
        background-color: #257b5e ;
        margin-left: 70px;
        width: 90px;
        border: solid #257b5e 2px;
        font-weight: bolder;
        color: white;
    }

    .font_1 {
        margin-top: 100px;
        color:white;
    }

    .font_2 {
        margin-top: 50px;
        color: #257b5e;
        font-weight: bolder;
        font-size: 30px;
        margin-bottom: 30px;
    }

    .title {
        font-size: 20px;
        margin: 0px auto 48px auto;
        font-weight: bolder;
        text-align: center;
        font-family: "Source Sans Pro";
    }
</style>