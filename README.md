前端：

- 管理端：oa-admin
- 员工端：oa-web

后端：oa-parent



其他说明：

- node-js版本：10.14.2
- JDK版本：8
- 注意：项目路径不用带中文，否则后端项目上传的工作流zip文件会找不到。
- 注：配置的域名如果需要跟上端口号才能使用，需要把 [微信公众平台 (qq.com)](https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index) 的网页账号配置中的域名也加上端口号。



问题：

- 审批中心一直刷新问题：使用pc端无此问题，手机端会有此问题。解决方法由b站其他小伙伴提供，不一定能行：
	- 方法1：在判断token前先加上这个：`if(token=="null"){token = '';}`
	- 方法2：后端权限不足时，返回209而不是208
	- 方法3：打断点debug时，在正确时机手动把openid添加到sys_user表中
