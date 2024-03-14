/* eslint-disable */
/**
 * Created by Administrator on 2018/3/1 0001.
 */

// 使用路由的懒加载加快页面的加载速度
const demo = () => import('./components/demo/demo');

const orga = () => import('./components/system_manage/organization_manage/orgbox');
const user = () => import('./components/system_manage/user_manage/userbox');
const role = () => import('./components/system_manage/role_manage/rolebox');
const menu = () => import('./components/system_manage/menu_manage/menubox');

const Sysmanage = () => import('./components/system_manage/sysmanege');
const post = () => import('./components/system_manage/post_manage/postbox');
const dictionary = () => import('./components/system_manage/dictionary_manage/dictbox');
const params = () => import('./components/system_manage/params_manage/parambox');
const log = () => import('./components/system_manage/log_manage/logbox');
const perm = () => import('./components/system_manage/permission/permbox');
const button = () => import('./components/system_manage/buttons_manege/buttonsbox');
const tasks = () => import('./components/system_manage/tasks_manage/taskbox');
const datasource = () => import('./components/system_manage/code_gen/datasource');
const code = () => import('./components/system_manage/code_gen/codebox');
const template = () => import('./components/system_manage/code_gen/template');
const templateTxt = () => import('./components/system_manage/code_gen/templateTxt');
const workflow = () => import('./components/system_manage/workflow/workflowAll');
const workflowDefi = () => import('./components/system_manage/workflow/workflowDefi');
const connection = () => import('./components/system_manage/modbus/connection');
const conDetail = () => import('./components/system_manage/modbus/conDetail');
const modDevice = () => import('./components/system_manage/modbus/device');
const modDevDetail = () => import('./components/system_manage/modbus/devDetail');



const routers = [ // 定义vue-router的路径和对应的组件，注意是component 不是components
    {
        path: '/',
        meta: {
            noRequireAuth: true  // 添加该字段，表示进入这个路由不需要登录
        },
        redirect: '/login' // 指定默认页面
    },
    {
        path: '/login',
        meta: {
            noRequireAuth: true  // 添加该字段，表示进入这个路由不需要登录
        },
        component: () => import('./components/login/login')
    },
    {
        path: '/preview',
        meta: {
            noRequireAuth: true  // 添加该字段，表示进入这个路由不需要登录
        },
        component: () => import('./viewsjkjc/preview')
    },
    {
    path: '/index',
    component: () => import('./components/index/index'),
    children: [
            {
                path: '/',
                redirect: '/home' // 指定默认页面
            },
            {
                path: '/home', component: () => import('./viewsjkjc/index/index')
            },
            {
                path: '/sysmanage', component: Sysmanage
            },
            {
                path: '/orga', component: orga
            },
            {
                path: '/user', component: user
            },
            {
                path: '/role', component: role
            },
            {
                path: '/perm', component: perm
            },
            {
                path: '/post', component: post
            },
            {
                path: '/dictionary', component: dictionary
            },
            {
                path: '/params', component: params
            },
            {
                path: '/log', component: log
            },
            {
                path: '/menu', component: menu
            },
            {
                path: '/button', component: button
            },
            {
                path: '/tasks', component: tasks
            },
            {
                path: '/datasource', component: datasource
            },
            {
                path: '/code', component: code
            },
            {
                path: '/template', component: template
            },
            {
                path: '/templateTxt', component: templateTxt
            },
            {
                path: '/workflow', component: workflow
            },
            {
                path: '/workflowDefi', component: workflowDefi
            },
            {
                path: '/connection', component: connection
            },
            {
                path: '/conDetail', component: conDetail
            },
            {
                path: '/device', component: connection
            },
            {
                path: '/devDetail', component: conDetail
            },
            {
                path: '/modDevice', component: modDevice
            },
            {
                path: '/modDevDetail', component: modDevDetail
            },
            {
                path: '/monitor',
                component: () => import('./viewsjkjc/monitor/index.vue')
            },
            {
                path: '/deviceWarning',
                component: ()=>import('./viewsjkjc/warning/deviceWarning.vue')
            },
            {
                path: '/otherWarning',
                component: ()=>import('./viewsjkjc/warning/otherWarning.vue')
            },
            {
                path: '/infoWarning',
                component: ()=>import('./viewsjkjc/warning/infoWarning.vue')
            },
            {
                path: '/deviceList',
                component: () => import('./viewsjkjc/device/deviceList.vue')
            },
            {
                path: '/patrolList',
                component: () => import('./viewsjkjc/device/patrolList.vue')
            },
            {
                path: '/gongwu',
                component: () => import('./viewsjkjc/gongwu/gongwu.vue')
            },
            {
                path: '/baseinfo',
                component: () => import('./viewsjkjc/bridge_baseinfo/list.vue')
            },
        ]
    },
    {
        path: '*',
        component: () => import('./components/404') // 指定默认页面
    }
];
export default routers;
