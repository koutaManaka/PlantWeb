// import { createLoadingComponent } from 'element-plus/es/components/loading/src/loading'
import {createRouter, createWebHistory} from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () =>  import ( '../pages/login.vue')
  },
  {
    path:'/',
    redirect:'/login',
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../pages/register.vue')
  },
  {
    path: '/home',
    name: 'home',
    component: () => import('../pages/home.vue')
  },
  {
    path: '/forget',
    name: 'forget',
    component: () =>import("../pages/forget.vue")
  }
]

const router = createRouter({
  //history: createWebHashHistory(),
  history: createWebHistory(),
  routes
})


// router.beforeEach((to,from,next) => {
//   if(to.path === '/register' || to.path === '/login'){ //若是进入登录与注册页面 ==> pass
//     next()
//     return ;
//   }else{
//     let LoginForm = localStorage.getItem("LoginForm")?JSON.parse(localStorage.getItem("LoginForm")):null;
//     if(LoginForm==null) {
//       alert("No account or password was entered");
//       next('/');
//       return ;
//     }
//     let userToken = LoginForm.token;
//     if(userToken == null || userToken == ' '){
//       alert("No account or password was entered");
//       next('/');
//       return ;
//     }else{
//       next();
//     }
//   }
// })

export default router

