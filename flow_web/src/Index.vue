<template>
	<div id="Indexapp">
		<div>
			<a href="javascript:void(0)" @click="jump('/processDefinition')">流程定义</a>
			<a href="javascript:void(0)" @click="jump('/myTask')">我的流程</a>
		</div>

		<router-view></router-view>
	</div>
</template>

<script>
	export default {
		name: 'Index',
		components: {},
		methods: {
			jump(myPath){
				this.$router.push({path: myPath})
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