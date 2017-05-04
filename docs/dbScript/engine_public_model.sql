INSERT INTO public.model (id, name, category, create_time, update_time, content, view_information, company_id, description, flag) VALUES (3, '修改', '1', '2017-04-18 11:11:34.000000', '2017-04-18 12:08:41.000000', '{
	"name": "测试数据",
	"description": "现在这是一个测试的model,准备用以测试使用.",
	"nodes": [
		{
			"type": "START",
			"id": 1,
			"name": "开始"
		},
		{
			"type": "USERTASK",
			"id": 2,
			"name": "节点1",
			"module": 1,
			"handle": 1,
			"assignees": "S_CurrentOperator"
		},
		{
			"type": "USERTASK",
			"id": 3,
			"name": "节点2",
			"module": 1,
			"handle": 2,
			"assignees": "R1"
		},
		{
			"type": "END",
			"id": 4,
			"name": "结束"
		}
	],
	"links": [
		{
			"id": 5,
			"from": 1,
			"to": 2
		},
		{
			"id": 6,
			"from": 2,
			"to": 3
		},
		{
			"id": 7,
			"from": 3,
			"to": 4
		}
	]
}', null, 1, null, 0);