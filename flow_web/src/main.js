// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import axios from 'axios'
import VueRouter from 'vue-router'
import processDefinition from './components/processDefinition';
import Index from './Index';

Vue.use(VueRouter);
Vue.prototype.$http = axios;

axios.defaults.timeout = 5000;                        //响应时间
Vue.config.productionTip = false;

const routes = [
	{path: '/processDefinition', component: processDefinition}
];

const router = new VueRouter({
	routes // （缩写）相当于 routes: routes
});


new Vue({
	el: '#app',
	router,
	template: '<Index/>',
	components: { Index }
});

