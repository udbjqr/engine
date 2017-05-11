<template>
	<div id="list" style="border: solid 1px black; height: 720px">
		<h2>{{msg}}</h2>
		<div v-for="item in loadList">
			<item :id="item.id" :name="item.name" @clickProcessDef="getData"></item>
		</div>
	</div>
</template>

<script>
	import item from './listItem';
	export default {
		name: 'list',
		data(){
			return {
				msg: '流程定义',
				loadList: []
			}
		},
		mounted() {
			this.$http.post('/designServlet/processDefinition', {'type': 'list'})
				.then(m => {
					this.loadList = m.data.data.list;
				});
		},
		methods: {
			getData: function (id) {
				this.$emit("clickProcessDef", id);
			}
		},
		components: {item}
	}
</script>

<style>
</style>
transition