<template>
	<div id="Indexapp">
		<div>
			<router-link to="/processDefinition">流程定义</router-link>
			<a href="#" @click="jump">我的流程</a>
		</div>

		<router-view></router-view>
	</div>
</template>

<script>
	export default {
		name: 'Index',
		components: {},
		methods:{
			jump(){
				this.$router.push({ path:'/processDefinition' })
			}
		}
	}
</script>

<style>
	#Indexapp {
		height: 100%;
		width: 100%;
		background: #F4F5F9;
	}
</style>