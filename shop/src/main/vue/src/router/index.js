import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/index.vue'
import ProductList from '../views/product/ProductList.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/product',
    children: [
      {
        path: 'product',
        name: 'ProductList',
        component: ProductList,
        meta: { title: '云里雾里' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

