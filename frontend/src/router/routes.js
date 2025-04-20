import {useStore} from "vuex";

const store = useStore();

const routes = [
  {
    path: '/',
    meta: {
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_USER']
    },
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/IndexPage.vue') }
    ]
  },
  {
    path: '/signals',
    meta: {
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_USER']
    },
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/SignalPage.vue') }
    ]
  },
  {
    path: '/admin',
    meta: {
      requiresAuth: true,
      roles: ['ROLE_ADMIN']
    },
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/AdminPage.vue') }
    ]
  },
  {
    path: '/settings',
    meta: {
      requiresAuth: true,
      roles: ['ROLE_ADMIN', 'ROLE_USER']
    },
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/UserSettingPage.vue') }
    ]
  },
  {
    path: '/login',
    meta: {requiresAuth: false},
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/LoginPage.vue') }
    ],
    beforeEnter: () => {
      if ($cookies.get("access_token")) return {path: '/'};
    }
  },
  {
    path: '/signup',
    meta: {requiresAuth: false},
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/SignupPage.vue') }
    ],
    beforeEnter: () => {
      if ($cookies.get("access_token")) return {path: '/'};
    }
  },
  {
    path: '/forget-password',
    meta: {requiresAuth: false},
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/ForgetPasswordPage.vue') }
    ],
    beforeEnter: () => {
      if ($cookies.get("access_token")) return {path: '/'};
    }
  },
  {
    path: '/reset-password',
    meta: {requiresAuth: false},
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/ResetPasswordPage.vue') }
    ],
    beforeEnter: () => {
      if ($cookies.get("access_token")) return {path: '/'};
    }
  },


  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
]

export default routes
