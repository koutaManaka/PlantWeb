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
    path: '/forget',
    name: 'forget',
    component: () =>import("../pages/forget.vue")
  },
  {
    path: '/data',
    name: 'data',
    component: () =>import("../pages/data.vue")
  },
  {
    path: '/manage',
    name: 'manage',
    component: () => import("../pages/manage.vue")
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


router.beforeEach((to,from,next) => {
  if(to.path === '/register' || to.path === '/login' || to.path === '/forget'){ //若是进入登录与注册页面 ==> pass
    next()
  }else{
    let user = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):null;
    if(user === null) {
      alert("No account or password was entered");
      next('/');
      return ;
    }
    let userToken = user.token;
    if(userToken === null || userToken === ' '){
      alert("No account or password was entered");
      next('/');
    }else{
      next();
    }
  }
})

export default router

