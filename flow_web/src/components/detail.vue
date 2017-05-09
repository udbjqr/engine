<template>
	<div id="detail" style="border: solid 1px black; height: 720px">
		<h1>ID:{{id}}</h1>
		<h1>名称:{{def.name}}</h1>
		<h1>描述:{{def.description}}</h1>
		<h1>分类:{{def.category}}</h1>
		<h1>版本:{{def.version}}</h1>

		<button @click="start">启动流程</button>
	</div>
</template>

<script>
	export default {
		name: 'detail',
		props: ['id'],
		data(){
			return {
				def: {'name': ''},
				oldid: -1
			}
		},
		beforeUpdate() {
			if (this.id !== this.oldid) {
				this.oldid = this.id;
				this.$http.post('/designServlet/processDefinition', {'type': 'load', 'definitionId': this.id})
					.then(m => {
						console.log(m.data);
						this.def = m.data.data.definition;
					});
			}
		},
		methods: {
			start(){
				this.$http.post('/designServlet/process', {'type': 'start', 'definitionId': this.id})
					.then(m => {
						console.log(m.data);
					});
			}
		}
	}
</script>
