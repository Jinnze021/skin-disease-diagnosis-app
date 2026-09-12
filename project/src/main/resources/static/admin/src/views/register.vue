<template>
	<article class="sign_in register_wrap">
		<div class="warp">
			<el-row>
				<el-col>
					<el-form :model="form" ref="form" label-width="80px" class="form">
						<h1>注册</h1>

						<el-form-item label="账号" prop="username">
							<el-input type="text" v-model="form.username" placeholder="请输入账号">
							</el-input>
						</el-form-item>

						<el-form-item label="密码" prop="password">
							<el-input type="password" v-model="form.password" placeholder="8 到 16 个字符"
								autocomplete="off">
							</el-input>
						</el-form-item>

						<el-form-item label="确认密码" prop="confirm_password">
							<el-input type="password" v-model="confirm_password" placeholder="8 到 16 个字符"
								autocomplete="off">
							</el-input>
						</el-form-item>

						<el-form-item label="昵称" prop="nickname">
							<el-input type="text" v-model="form.nickname" placeholder="请输入昵称">
							</el-input>
						</el-form-item>

						<el-form-item label="邮箱" prop="email">
							<el-input type="email" v-model="form.email" placeholder="例：test@test.com!">
							</el-input>
						</el-form-item>



						<el-button class="float-right  rs_register" type="primary" @click="submit()" >注册</el-button>
				
						<el-button  class="float-right  rs_back" @click="$router.push('./login')">返回</el-button>
					</el-form>
				</el-col>
			</el-row>
		</div>
	</article>
</template>

<script>
	import mixin from "@/mixins/page.js";

	export default {
		mixins: [mixin],
		data: function() {
			return {
				oauth: { signIn: false },
				url_submit: "~/api/user/register?",
				web: this.$store.state.web,
				form: {
					username: "",
					password: "",
					nickname: "",
					email: "",
					user_group: "注册用户",
					avatar: ""
				},
				confirm_password: ""
			}
		},
		methods: {
			async submit(param, func) {
				param = param || this.form;
				param.avatar = param.avatar || "/api/upload/default_avatar.jpg";
				var request = Object.assign({}, param);
				var message = await this.submit_check(request);
				if (message) {
					this.$toast(message, "danger");
					return;
				}
				return this.events("submit_main", request, func);
			},
			async submit_check(param) {
				var emailRegular = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				if (!param.username) return "账号不能为空";
				if (param.username.length > 16 || param.username.length < 5) return "账号长度应为5到16个字符之间！";
				if (!param.password) return "密码不能为空!";
				if (param.password.length > 16 || param.password.length < 8) return "密码长度应为8到16个字符之间！";
				if ((param.nickname && param.nickname.length > 12) || param.nickname.length < 2) return "昵称长度应为2个字符到12个字符之间";
				if (param.email && !emailRegular.test(param.email)) return "请输入正确的邮箱地址 例：test@test.com!";
				if (param.password !== this.confirm_password) return "确认密码与密码不一致！";
				return null;
			},
			submit_after() {
				this.$toast("注册成功!", "success");
				this.$store.commit("quit");
				this.$router.push("/login");
			}
		},
		components: {}
	}
</script>

<style>
	.sign_in {
		box-sizing: border-box;
		height: 100vh;
		padding: .5rem;
		width: 100%;
		background: url(../../public/img/bg.jpg) no-repeat;
		background-size: 100% 100%;
		color: #fff;
	}

	.sign_in h1,
	.sign_in .btns {
		text-align: center;
	}

	.sign_in .btns .el-button {
		width: 40%;
	}

	.sign_in .el-form-item__label {
		color: #fff;
	}

	.sign_in .form {
		box-sizing: border-box;
		padding: 1rem 3rem;
		margin: auto;
		margin-top: 13rem;
		width: 100%;
		max-width: 25rem;
		box-shadow: 0 0 0 1px hsla(240, 0%, 100%, .3) inset,
			0 .5em 1em rgba(50, 50, 50, 0.6);
		text-shadow: 0 1px 1px hsla(240, 0%, 100%, .5);
		overflow: hidden;
		/* background-color: rgba(255, 255, 255, .5); */
		border-radius: 0.5rem;
	}

	.sign_in .form:after {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(255, 255, 255, 0.8);
		z-index: -1;

		background-image: url(../../public/img/bg.jpg);
		background-position: center top;
		background-size: cover;
		background-attachment: fixed;
		-webkit-filter: blur(20px);
		-moz-filter: blur(20px);
		-ms-filter: blur(20px);
		-o-filter: blur(20px);
		filter: blur(20px);
	}
	.img_multiple{
		overflow: hidden;
	}
	.img_multiple .img_block{
		float: left;
		margin-right: 5px;
		margin-bottom: 5px;
		position: relative;
	}
	.img_multiple .img_block img{
		height: 100px;
		width: auto;
	}
	.img_multiple .img_del{
		position: absolute;
		top: 5px;
		right: 5px;
		width: 20px;
		height: 20px;
		background: #0000008a;
		color: #fff;
		line-height: 20px;
		text-align: center;
		border-radius: 100%;
		cursor: pointer;
	}
</style>
