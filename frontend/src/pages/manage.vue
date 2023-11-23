<script setup>
    import {onMounted, reactive, ref} from "vue";
    import request from "@/utils/request";
    import {ElMessage} from "element-plus";
    const tableData = ref([])
    const total = ref(0)
    const pageNum = ref(1)
    const pageSize = ref(5)
    const username = ref()
    const email = ref()
    const apiKey = ref()
    const form = ref({})
    const Form = ref({})
    const multipleSelection = ref([])
    const dialogFormVisible = ref(false)
    const dialogformVisible = ref(false)
    const getUser = () => {
        request.get("/user/page.do",{
            params:{
                pageNum: pageNum.value,
                pageSize:pageSize.value,
                username: username.value,
                email: email.value,
                apiKey: apiKey.value
            }
        }).then(res => {
            tableData.value = res.data.data
            total.value = res.data.total
        })
    }

    const reset = () =>{
        username.value = ""
        apiKey.value = ""
        email.value = ""
        getUser()
    }

    const handleSizeChange = (pageSizes) => {
        pageSize.value=pageSizes
        getUser()
    }

    const handleCurrentChange = (pageNums) => {
        pageNum.value=pageNums
        getUser()
    }

    const handleAdd = ()=> {
        dialogFormVisible.value = true
        Form.value = {}
    }

    const insertUser= ()=> {
        request.post("/user/insert.do",Form.value).then(res =>{
            if (res) {
                ElMessage.success("Add Successfully");
                dialogFormVisible.value = false
                getUser()
            } else {
                ElMessage.error("Add unsuccessfully");
            }
        })
    }

    const handleEdit = (row)=> {
        dialogformVisible.value = true
        form.value = row
    }

    const updateUser = ()=> {
        request.post("/user/edit.do",form.value).then(res =>{
            if (res) {
                ElMessage.success("Edit Successfully");
                dialogformVisible.value = false
                getUser()
            } else {
                ElMessage.error("Edit unsuccessfully");
            }
        })
    }

    const del = (id)=> {
        request.delete("/user/" + id).then(res =>{
            if (res) {
                ElMessage.success("Delete Successfully");
                getUser()
            } else {
                ElMessage.error("Delete unsuccessfully");
            }
        })
    }
    const handleSelectionChange = (val)=> {
        multipleSelection.value = val
    }

    const deleteBatch = ()=> {
        let ids = multipleSelection.value.map(v => v.id)
        request.post("/user/batch.do",ids).then(res =>{
            if (res) {
                ElMessage.success("Delete Successfully");
                getUser()
            } else {
                ElMessage.error("Delete unsuccessfully");
            }
        })
    }

    onMounted(() => {
        getUser();
    });



</script>

<template>
    <el-container class="layout-container-demo" style="height: 100%">
        <el-aside width="210px" style="height: 100%;background-color:rgb(48,65,86)">
            <el-scrollbar>
                <el-menu :default-openeds="['1','2']" style="height: 100%; overflow-x: hidden;"
                         background-color="rgb(48,65,86)" text-color="#fff" active-text-color="#ffd04b">
                    <div style="height: 60px; line-height: 60px; text-align:center">
                        <b>智慧种植系统后台</b>
                    </div>
                    <el-menu-item index="0">
                        <el-icon>
                            <search/>
                        </el-icon>
                        <span>查看用户</span>
                    </el-menu-item>
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
                                <el-dropdown-item><span style="text-decoration-line: none"
                                                        @click="Exit()">退出登录</span></el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
<!--                    <span>{{user.username}}</span>-->
                </div>
            </el-header>
            <el-main>
                <el-row style="padding: 10px 0;margin-top: 20px">
                    <el-col span="12" style="margin-left: 20px">
                        <el-input  placeholder="输入用户名" style="width: 200px;margin-right: 10px" v-model="username" >
                            <template #suffix>
                            <el-icon class="el-input__icon"><search /></el-icon>
                            </template>
                        </el-input>
                        <el-input  placeholder="输入密钥" style="width: 200px;margin-right: 10px" v-model="apiKey">
                            <template #suffix>
                                <el-icon class="el-input__icon"><lock /></el-icon>
                            </template>
                        </el-input>
                        <el-input  placeholder="输入邮箱" style="width: 200px" v-model="email">
                            <template #suffix>
                                <el-icon class="el-input__icon"><message /></el-icon>
                            </template>
                        </el-input>
                        <el-button style="margin-left: 10px" type="primary" @click="getUser" >搜索</el-button>
                        <el-button style="margin-left: 10px" type="warning" @click="reset">重置</el-button>
                    </el-col>
                </el-row>
                <el-row style="margin-bottom: 10px">
                    <el-col span="12" style="margin-left: 10px">
                        <el-button style="margin-left: 10px" type="primary" @click="handleAdd">新增<el-icon style="margin-left: 5px"><circlePlus/></el-icon></el-button>
                        <el-popconfirm
                                width="250"
                                confirm-button-text="是的"
                                cancel-button-text="我再想想"
                                :icon="InfoFilled"
                                icon-color="#626AEF"
                                title="您确定要批量删除这些数据吗?"
                                @confirm="deleteBatch"
                        >
                            <template #reference>
                                <el-button style="margin-left: 10px" type="danger">批量删除 <el-icon style="margin-left: 5px"><remove/></el-icon></el-button>
                            </template>
                        </el-popconfirm>
                    </el-col>
                </el-row>
                <el-table :data="tableData" style="width: 98%;margin-left: 20px" border stripe @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55" />
                    <el-table-column prop="id" label="ID" width="90" />
                    <el-table-column prop="username" label="用户名" width="120" />
                    <el-table-column prop="role" label="权限" width="120" />
                    <el-table-column prop="email" label="邮箱" width="180" />
                    <el-table-column prop="apiKey" label="密钥" width="280" />
                    <el-table-column prop="deviceId" label="设备ID" />
                    <el-table-column label="操作">
                        <template #default="scope">
                            <el-button type="success" @click="handleEdit(scope.row)">编辑<el-icon style="margin-left: 5px"><edit/></el-icon></el-button>
                            <el-popconfirm
                                    width="220"
                                    confirm-button-text="是的"
                                    cancel-button-text="我再想想"
                                    :icon="InfoFilled"
                                    icon-color="#626AEF"
                                    title="您确定要删除吗?"
                                    @confirm="del(scope.row.id)"
                            >
                                <template #reference>
                                    <el-button type="danger">删除<el-icon style="margin-left: 5px"><remove/></el-icon></el-button>
                                </template>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="block" style="margin-left: 20px;margin-top: 10px">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :page-sizes="[5, 10, 15, 20]"
                            :page-size="5"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total=total>
                    </el-pagination>
                </div>
                <el-dialog v-model="dialogFormVisible"  width="500px" title="用户信息">
                    <el-form label-width="120px" size="default" style="margin-right: 65px">
                        <el-form-item label="用户名" :label-width="formLabelWidth" >
                            <el-input v-model="Form.username" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="权限" :label-width="formLabelWidth">
                            <el-input v-model="Form.role" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" :label-width="formLabelWidth">
                            <el-input v-model="Form.email" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="密钥" :label-width="formLabelWidth">
                            <el-input v-model="Form.apiKey" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="设备ID" :label-width="formLabelWidth">
                            <el-input v-model="Form.deviceId" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="insertUser">确 定</el-button>
                    </div>
                </el-dialog>
                <el-dialog v-model="dialogformVisible"  width="500px" title="用户信息">
                    <el-form label-width="120px" size="default" style="margin-right: 65px">
                        <el-form-item label="用户名" :label-width="formLabelWidth" >
                            <el-input v-model="form.username" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="权限" :label-width="formLabelWidth">
                            <el-input v-model="form.role" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" :label-width="formLabelWidth">
                            <el-input v-model="form.email" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="密钥" :label-width="formLabelWidth">
                            <el-input v-model="form.apiKey" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="设备ID" :label-width="formLabelWidth">
                            <el-input v-model="form.deviceId" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogformVisible = false">取 消</el-button>
                        <el-button type="primary" @click="updateUser">确 定</el-button>
                    </div>
                </el-dialog>
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