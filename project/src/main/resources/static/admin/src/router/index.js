import Vue from 'vue';
import VueRouter from 'vue-router';
import index from '../views/index.vue';
import login from '../views/login.vue';
import forgot from '../views/forgot.vue';
Vue.use(VueRouter)

const routes = [
    // 主页
    {
        path: '/',
        name: 'index',
        component: index,
        meta: {
            index: 0,
            title: '首页'
        }
    },

    // 登录
    {
        path: '/login',
        name: 'login',
        component: login,
        meta: {
            index: 0,
            title: '登录'
        }
    },

    	
	
    // 忘记密码
    {
        path: '/forgot',
        name: "forgot",
        component: forgot,
        meta: {
            index: 0,
            title: '忘记密码'
        }
    },

    // 修改密码
    {
        path: '/user/password',
        name: "password",
        component: () => import("../views/user/password.vue"),
        meta: {
            index: 0,
            title: '修改密码'
        }
    },

    // 视频播放页
    {
        path: "/media/video",
        name: "video",
        component: () => import('../views/media/video.vue'),
        meta: {
            index: 0,
            title: "视频"
        }
    },

    // 音频播放页
    {
        path: "/media/audio",
        name: "audio",
        component: () => import('../views/media/audio.vue'),
        meta: {
            index: 0,
            title: "音频"
        }
    },

    
    
                // 轮播图路由
        {
            path: '/slides/table',
            name: 'slides_table',
            component: () => import('../views/slides/table.vue'),
            meta: {
                index: 0,
                title: '轮播图列表'
            }
        },
        {
            path: '/slides/view',
            name: 'slides_view',
            component: () => import('../views/slides/view.vue'),
            meta: {
                index: 0,
                title: '轮播图详情'
            }
        },
                    // 文章路由
            {
                path: '/article/table',
                name: 'article_table',
                component: () => import('../views/article/table.vue'),
                meta: {
                    index: 0,
                    title: '新闻资讯列表'
                }
            },
            {
                path: '/article/view',
                name: 'article_view',
                component: () => import('../views/article/view.vue'),
                meta: {
                    index: 0,
                    title: '新闻资讯详情'
                }
            },

            // 文章分类路由
            {
                path: '/article_type/table',
                name: 'article_type_table',
                component: () => import('../views/article_type/table.vue'),
                meta: {
                    index: 0,
                    title: '新闻资讯分类列表'
                }
            },
            {
                path: '/article_type/view',
                name: 'article_type_view',
                component: () => import('../views/article_type/view.vue'),
                meta: {
                    index: 0,
                    title: '新闻资讯分类详情'
                }
            },
                            
    
            // 论坛路由
        {
            path: '/forum/table',
            name: 'forum_table',
            component: () => import('../views/forum/table.vue'),
            meta: {
                index: 0,
                title: '论坛列表'
            }
        },
        {
            path: '/forum/view',
            name: 'forum_view',
            component: () => import('../views/forum/view.vue'),
            meta: {
                index: 0,
                title: '论坛详情'
            }
        },

        // 论坛分类路由
        {
            path: '/forum_type/table',
            name: 'forum_type_table',
            component: () => import('../views/forum_type/table.vue'),
            meta: {
                index: 0,
                title: '论坛分类列表'
            }
        },
        {
            path: '/forum_type/view',
            name: 'forum_type_view',
            component: () => import('../views/forum_type/view.vue'),
            meta: {
                index: 0,
                title: '论坛分类详情'
            }
        },
    
            // 公告路由
        {
            path: '/notice/table',
            name: 'notice_table',
            component: () => import('../views/notice/table.vue'),
            meta: {
                index: 0,
                title: '公告信息列表'
            }
        },
        {
            path: '/notice/view',
            name: 'notice_view',
            component: () => import('../views/notice/view.vue'),
            meta: {
                index: 0,
                title: '公告信息详情'
            }
        },
            	    
            // 评论路由
        {
            path: '/comment/table',
            name: 'comment_table',
            component: () => import('../views/comment/table.vue'),
            meta: {
                index: 0,
                title: '评论列表'
            }
        },
        {
            path: '/comment/view',
            name: 'comment_view',
            component: () => import('../views/comment/view.vue'),
            meta: {
                index: 0,
                title: '评论详情'
            }
        },
        	            // 注册用户路由
        {
            path: '/registered_user/table',
            name: 'registered_user_table',
            component: () => import('../views/registered_user/table.vue'),
            meta: {
                index: 0,
                title: '注册用户列表'
            }
        },
        {
            path: '/registered_user/view',
            name: 'registered_user_view',
            component: () => import('../views/registered_user/view.vue'),
            meta: {
                index: 0,
                title: '注册用户详情'
            }
        },
						            // 皮肤检测路由
        {
            path: '/skin_detection/table',
            name: 'skin_detection_table',
            component: () => import('../views/skin_detection/table.vue'),
            meta: {
                index: 0,
                title: '皮肤检测列表'
            }
        },
        {
            path: '/skin_detection/view',
            name: 'skin_detection_view',
            component: () => import('../views/skin_detection/view.vue'),
            meta: {
                index: 0,
                title: '皮肤检测详情'
            }
        },
						            // 知识科普路由
        {
            path: '/knowledge_popularization/table',
            name: 'knowledge_popularization_table',
            component: () => import('../views/knowledge_popularization/table.vue'),
            meta: {
                index: 0,
                title: '知识科普列表'
            }
        },
        {
            path: '/knowledge_popularization/view',
            name: 'knowledge_popularization_view',
            component: () => import('../views/knowledge_popularization/view.vue'),
            meta: {
                index: 0,
                title: '知识科普详情'
            }
        },
						            // 医院信息路由
        {
            path: '/hospital_information/table',
            name: 'hospital_information_table',
            component: () => import('../views/hospital_information/table.vue'),
            meta: {
                index: 0,
                title: '医院信息列表'
            }
        },
        {
            path: '/hospital_information/view',
            name: 'hospital_information_view',
            component: () => import('../views/hospital_information/view.vue'),
            meta: {
                index: 0,
                title: '医院信息详情'
            }
        },
						            // 在线咨询路由
        {
            path: '/online_consultation/table',
            name: 'online_consultation_table',
            component: () => import('../views/online_consultation/table.vue'),
            meta: {
                index: 0,
                title: '在线咨询列表'
            }
        },
        {
            path: '/online_consultation/view',
            name: 'online_consultation_view',
            component: () => import('../views/online_consultation/view.vue'),
            meta: {
                index: 0,
                title: '在线咨询详情'
            }
        },
						    	    // 用户路由
    {
        path: '/user/table',
        name: 'user_table',
        component: () => import('../views/user/table.vue'),
        meta: {
            index: 0,
            title: '用户列表'
        }
    },
    {
        path: '/user/view',
        name: 'user_view',
        component: () => import('../views/user/view.vue'),
        meta: {
            index: 0,
            title: '用户详情'
        }
    },
    {
        path: '/user/info',
        name: 'user_info',
        component: () => import('../views/user/info.vue'),
        meta: {
            index: 0,
            title: '个人信息'
        }
    },
    // 用户组路由
    {
        path: '/user_group/table',
        name: 'user_group_table',
        component: () => import('../views/user_group/table.vue'),
        meta: {
            index: 0,
            title: '用户组列表'
        }
    },
    {
        path: '/user_group/view',
        name: 'user_group_view',
        component: () => import('../views/user_group/view.vue'),
        meta: {
            index: 0,
            title: '用户组详情'
        }
    }
]

const router = new VueRouter({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    let token = to.query.token;
    if (token) {
        $.db.set("token", token, 120);
    }
    next();
})

router.afterEach((to, from, next) => {
    let title = "皮肤病检测小程序-admin";
    document.title = title;
})

export default router
