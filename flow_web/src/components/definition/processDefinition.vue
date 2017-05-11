<template>
	<div id="processDefinition" style="width:100%; white-space:nowrap;display:flex">
		<span style="display: inline-block; vertical-align: top; width:150px">
			<list @clickProcessDef="getData"></list>
    </span>
		<span style="display: inline-block; vertical-align: top; flex:1">
      <detail :id="currentId"></detail>
    </span>
	</div>
</template>

<script>
	import list from './list';
	import detail from './detail';

	export default {
		name: 'processDefinition',
		data(){
			return {
				currentId: -1
			}
		},
		components: {list, detail},
		methods: {
			getData: function (id) {
				this.currentId = id;
			}
		}
	}
</script>

<style>
</style>