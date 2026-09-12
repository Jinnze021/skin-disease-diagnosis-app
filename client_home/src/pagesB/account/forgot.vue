<template>
  <view id="account_forgot" class="user_account account_forgot">
	<view class="container">
		<view class="custom-1"></view>
		<view class="back-btn" @click="navBack"></view>
		<view class="custom-3"></view>
		<view class="wrapper">
		  <view class="left-top-sign">FORGOT</view>
		  <view class="welcome">找回密码</view>
		  <view class="input-content">
		    <view class="input-item">
		      <text class="tit">密码</text>
		      <input
		        type="password"
		        v-model="form.password"
		        placeholder="请输入8-16位新密码"
		        placeholder-class="input-empty"
		        maxlength="20"
		        password
		        data-key="password"
		      />
		    </view>
		    <view class="input-item">
		      <text class="tit">确认密码</text>
		      <input
		        type="password"
		        v-model="confirm_password"
		        placeholder="请再次输入8-16位新密码"
		        placeholder-class="input-empty"
		        maxlength="20"
		        password
		        data-key="password"
		      />
		    </view>
		    <view class="input-item">
		      <text class="tit">邮箱</text>
		      <input type="text" v-model="form.email" placeholder="请输入邮箱" />
		    </view>
		    <view class="input-item">
		      <text class="tit">验证码</text>
		      <input
		        style="width: 50%"
		        type="text"
		        v-model="form.code"
		        placeholder="请输入验证码"
		        maxlength="10"
		      />
		      <button
		        class="btn_send"
		        type="primary"
		        style="width: 40%; margin: 0 10px"
		        size="mini"
		        @click="get_code()"
		      >
		        发送
		      </button>
		    </view>
		  </view>
		  <button class="confirm-btn" @click="forgot">找回密码</button>
		  <view class="register-section">
		    还没有账号?<view @click="$navTo('/pagesB/account/register')" class="text">立即注册</view>
		  </view>
		</view>
	</view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: { password: '', email: '', code: '' },
      confirm_password: '',
    };
  },
  methods: {
    validEmail(email) {
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email || '');
    },
    get_code() {
      if (!this.validEmail(this.form.email)) {
        this.$toast('请输入正确的邮箱地址', 'error');
        return;
      }
      this.$post('~/api/code_token/password-reset/request', { email: this.form.email }, (res) => {
        if (res.result) {
          this.$toast(res.result, 'success');
        } else if (res.error) {
          this.$toast(res.error.message, 'error');
        }
      });
    },
    forgot() {
      const message = this.submit_check();
      if (message) {
        this.$toast(message, 'error');
        return;
      }
      this.$post('~/api/user/password-reset/confirm', {
        email: this.form.email,
        code: this.form.code,
        new_password: this.form.password,
      }, (res) => {
        if (res.result) {
          this.$toast('密码修改成功，请重新登录', 'success');
          this.$navTo('/pagesB/account/login');
        } else if (res.error) {
          this.$toast(res.error.message, 'error');
        }
      });
    },
    submit_check() {
      if (!this.validEmail(this.form.email)) return '请输入正确的邮箱地址';
      if (!/^\d{6}$/.test(this.form.code || '')) return '请输入6位验证码';
      if (!this.form.password || this.form.password.length < 8 || this.form.password.length > 16) return '密码长度应为8到16个字符';
      if (this.form.password !== this.confirm_password) return '密码和确认密码不一致';
      return '';
    },
    navBack() {
      uni.navigateBack();
    },
  },
};
</script>

<style lang="scss" scoped>
@import 'styles/pagesB/index.scss';
</style>
