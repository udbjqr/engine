<template>
	<a href="javascript:void(0)" v-on:click="clickProcessDef">{{name}}</a>
</template>

<script>
	export default {
		name: 'listItem',
		props: ['name', 'id'],
		data(){
			return {}
		},
		methods: {
			clickProcessDef: function () {
				this.$emit("clickProcessDef", this.id);
			}
		}
	}
</script>

<style>

</style>