<template>
  <article class="sign_in forgot_wrap">
    <div class="warp">
      <el-row>
        <el-col>
          <el-form :model="form" ref="form" label-width="80px" class="form">
            <h1>忘记密码</h1>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱">
              </el-input>
            </el-form-item>

            <el-form-item label="验证码" prop="code">
              <el-input v-model="form.code" placeholder="请输入验证码">
                <el-button @click="send()" slot="append">发送验证码</el-button>
              </el-input>
            </el-form-item>

            <el-form-item label="新密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入新密码" show-password>
              </el-input>
            </el-form-item>

            <el-form-item label="确认密码" prop="confirm_password">
              <el-input v-model="confirm_password" placeholder="请再次输入新密码" show-password>
              </el-input>
            </el-form-item>

            <div class="btns">
              <el-button class="float-right  fg_submit" type="primary" @click="submit()">提交</el-button>
              <!-- <router-link to="./login">取消</router-link> -->
              <el-button class="float-right  fg_cancle" @click="$router.push('./login')">取消</el-button>
            </div>

          </el-form>
        </el-col>
      </el-row>
    </div>
  </article>
</template>

<script>
  export default {
    data: function () {
      return {
        confirm_password: "",
        form: { email: "", password: "", code: "" }
      }
    },
    methods: {
      validEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email || "");
      },
      send() {
        if (!this.validEmail(this.form.email)) {
          this.$toast("请输入正确的邮箱地址", "error");
          return;
        }
        this.$post("~/api/code_token/password-reset/request", {email: this.form.email}, (res) => {
          if (res.result) this.$toast(res.result, "success");
          else if (res.error) this.$toast(res.error.message, "error");
        });
      },
      submit() {
        if (!this.validEmail(this.form.email)) {
          this.$toast("请输入正确的邮箱地址", "error");
          return;
        }
        if (!/^\d{6}$/.test(this.form.code || "")) {
          this.$toast("请输入6位验证码", "error");
          return;
        }
        if (!this.form.password || this.form.password.length < 8 || this.form.password.length > 16) {
          this.$toast("密码长度应为8到16个字符", "error");
          return;
        }
        if (this.form.password !== this.confirm_password) {
          this.$toast("密码和确认密码不一致", "error");
          return;
        }
        this.$post("~/api/user/password-reset/confirm", {
          email: this.form.email,
          code: this.form.code,
          new_password: this.form.password
        }, (res) => {
          if (res.result) {
            this.$toast("密码修改成功，请重新登录", "success");
            this.$router.push("/login");
          } else if (res.error) {
            this.$toast(res.error.message, "error");
          }
        });
      }
    }
  }
</script>

<style>
  .sign_in {
    box-sizing: border-box;
    height: 100vh;
    padding: .5rem;
    width: 100%;
    background: url("../../public/img/bg.jpg") no-repeat;
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
    margin-top: 15rem;
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

    background-image: url("../../public/img/bg.jpg");
    background-position: center top;
    background-size: cover;
    background-attachment: fixed;
    -webkit-filter: blur(20px);
    -moz-filter: blur(20px);
    -ms-filter: blur(20px);
    -o-filter: blur(20px);
    filter: blur(20px);
  }
</style>
