<template>
	<div id="detail" style="border: solid 1px black; height: 720px">
		<h1>ID:{{id}}</h1>
		<h1>流程:{{task.execution_id}}</h1>

		<button @click="start">启动流程</button>
	</div>
</template>

<script>
	export default {
		name: 'detail',
		props: ['id'],
		data(){
			return {
				task: {'name': ''},
				oldid: -1
			}
		},
		beforeUpdate() {
			if (this.id !== this.oldid) {
				this.oldid = this.id;
				this.$http.post('/designServlet/task', {'type': 'load', 'taskId': this.id})
					.then(m => {
						this.task = m.data.data.task;
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
