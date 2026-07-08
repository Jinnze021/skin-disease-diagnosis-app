<template>
  <view class="page_diy_view page_online_consultation_view" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>在线咨询详情</tn-nav-bar>
    <view id="page_diy_view" class="page-from">
      <uni-forms :modelValue="form" v-if="is_view()" labelWidth="70px">

        <uni-forms-item v-if="$check_field('get','registered_user') || ($check_field('add','registered_user') || $check_field('set','registered_user'))" label="注册用户" name="registered_user">
              <uni-data-select
              id="form_registered_user"
              v-model="form['registered_user']"
              :localdata="list_user_registered_user"
              :clear="!disabledObj['registered_user_isDisabled']"
              :disabled="disabledObj['registered_user_isDisabled']"
              v-if="(form['online_consultation_id'] && $check_field('set', 'registered_user')) || (!form['online_consultation_id'] && $check_field('add', 'registered_user'))"
			   style="width: 100%;"
          ></uni-data-select>
          <uni-data-select
              v-model="form['registered_user']"
              :localdata="list_user_registered_user"
              :clear="false"
              :disabled="true"
              v-else-if="$check_field('get', 'registered_user')"
              id="registered_user"
			  style="width: 100%;"
          ></uni-data-select>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','user_name') || ($check_field('add','user_name') || $check_field('set','user_name'))" label="用户姓名" name="user_name">
                      <uni-easyinput type="text" v-model="form['user_name']" v-if="(form['online_consultation_id'] && $check_field('set','user_name')) || (!form['online_consultation_id'] && $check_field('add','user_name'))" :disabled="disabledObj['user_name_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','user_name')">
            {{ form['user_name'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','advisory_title') || ($check_field('add','advisory_title') || $check_field('set','advisory_title'))" label="咨询标题" name="advisory_title">
                      <uni-easyinput type="text" v-model="form['advisory_title']" v-if="(form['online_consultation_id'] && $check_field('set','advisory_title')) || (!form['online_consultation_id'] && $check_field('add','advisory_title'))" :disabled="disabledObj['advisory_title_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','advisory_title')">
            {{ form['advisory_title'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','consulting_category') || ($check_field('add','consulting_category') || $check_field('set','consulting_category'))" label="咨询类别" name="consulting_category">
                      <uni-easyinput type="text" v-model="form['consulting_category']" v-if="(form['online_consultation_id'] && $check_field('set','consulting_category')) || (!form['online_consultation_id'] && $check_field('add','consulting_category'))" :disabled="disabledObj['consulting_category_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','consulting_category')">
            {{ form['consulting_category'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','consulting_content') || ($check_field('add','consulting_content') || $check_field('set','consulting_content'))" label="咨询内容" name="consulting_content">
              <uni-easyinput type="textarea" v-model="form['consulting_content']" v-if="(form['online_consultation_id'] && $check_field('set', 'consulting_content')) || (!form['online_consultation_id'] && $check_field('add', 'consulting_content'))" :disabled="disabledObj['consulting_content_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'consulting_content')">
            {{ form['consulting_content'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','reply_content') || ($check_field('add','reply_content') || $check_field('set','reply_content'))" label="回复内容" name="reply_content">
              <uni-easyinput type="textarea" v-model="form['reply_content']" v-if="(form['online_consultation_id'] && $check_field('set', 'reply_content')) || (!form['online_consultation_id'] && $check_field('add', 'reply_content'))" :disabled="disabledObj['reply_content_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'reply_content')">
            {{ form['reply_content'] }}
          </text>
            </uni-forms-item>
      </uni-forms>
      <view class="form-footer" v-if="$check_action('/online_consultation/view','set') || ($check_action('/online_consultation/view','add') || $check_option('/online_consultation/table','examine'))">
        <view class="me-btn btn-submit" @click="submit_()"> 提交 </view>
        <view class="me-btn btn-cancel" @click="cancel()">取消</view>
      </view>
      <view class="form_button" v-else>
        <view class="me-btn btn-cancel" @click="cancel()">返回</view>
      </view>
    </view>
  </view>
</template>

<script>
import mixin from '@/libs/mixins/page.js';
                        import { uploadFilePathApi, uploadFileApi } from '@/api/common.js';
export default {
  mixins: [mixin],
  data() {
    return {
	  today: new Date().toISOString().split('T')[0],
      field: "online_consultation_id",
      url_add: "~/api/online_consultation/add?",
      url_set: "~/api/online_consultation/set?",
      url_get_obj: "~/api/online_consultation/get_obj?",
      url_upload: "~/api/online_consultation/upload?",
      registered_user: null,
      user_name: null,
      advisory_title: null,
      consulting_category: null,
      consulting_content: null,
      reply_content: null,
      query: {
        "online_consultation_id": 0,
      },
      form: {
          "registered_user": 0, // 注册用户
            "user_name":  '', // 用户姓名
            "advisory_title":  '', // 咨询标题
            "consulting_category":  '', // 咨询类别
            "consulting_content":  '', // 咨询内容
            "reply_content":  '', // 回复内容
          "online_consultation_id": 0, // ID
      },
      disabledObj:{
          "registered_user_isDisabled": false,
            "user_name_isDisabled": false,
            "advisory_title_isDisabled": false,
            "consulting_category_isDisabled": false,
            "consulting_content_isDisabled": false,
            "reply_content_isDisabled": false,
        },
              // 用户列表
      list_user_registered_user: [],
                  // 用户组
      group_user_registered_user: "",
                                                        };
  },
  methods: {
    /**
     * 提交前验证事件
     * @param {Object} 请求参数
     * @return {String} 验证成功返回null, 失败返回错误提示
     */
    async submit_check(param) {
                                                                                                      return null;
    },
    changeLog(v, value) {
      this.form[value] = v;
    },
    uploadFile_(type) {
      // #ifdef APP-VUE
      uni.chooseFile({
        count: 1,
        success: (chooseImageRes) => {
          const tempFilePaths = chooseImageRes.tempFilePaths;
          this.successChoose(tempFilePaths[0], type);
        },
      });
      // #endif
      // #ifdef !APP-VUE
      if (
        uni.getSystemInfoSync().uniPlatform != 'mp-weixin' ||
        uni.getSystemInfoSync().platform == 'devtools'
      ) {
        uni.chooseImage({
          count: 1,
          success: (chooseImageRes) => {
            const tempFilePaths = chooseImageRes.tempFilePaths;
            this.successChoose(tempFilePaths[0], type);
          },
        });
      } else {
        wx.chooseMessageFile({
          count: 1,
          success: (chooseImageRes) => {
            const tempFilePaths = chooseImageRes.tempFiles;
            this.successChoose(tempFilePaths[0].path, type);
          },
        });
      }
      // #endif
    },
    successChoose(filePath, type) {
      uni.uploadFile({
        url: this.$fullUrl('~/api/online_consultation/upload?'), //仅为示例，非真实的接口地址
        filePath,
        name: 'file',
        success: (uploadFileRes) => {
          if (uploadFileRes.data.error) {
            uni.showToast({ title: uploadFileRes.data.error.message, icon: 'none' });
          } else {
            this[type] = JSON.parse(uploadFileRes.data).result.url;
          }
        },
      });
    },
    close_(type) {
      if (type == 'registered_user') this['registered_user'] = this.form['registered_user'] = "";
      if (type == 'user_name') this['user_name'] = this.form['user_name'] = "";
      if (type == 'advisory_title') this['advisory_title'] = this.form['advisory_title'] = "";
      if (type == 'consulting_category') this['consulting_category'] = this.form['consulting_category'] = "";
      if (type == 'consulting_content') this['consulting_content'] = this.form['consulting_content'] = "";
      if (type == 'reply_content') this['reply_content'] = this.form['reply_content'] = "";
    },
    submit_() {
      if (this['registered_user'] !== null) this.form['registered_user'] = this['registered_user']
      if (this['user_name'] !== null) this.form['user_name'] = this['user_name']
      if (this['advisory_title'] !== null) this.form['advisory_title'] = this['advisory_title']
      if (this['consulting_category'] !== null) this.form['consulting_category'] = this['consulting_category']
      if (this['consulting_content'] !== null) this.form['consulting_content'] = this['consulting_content']
      if (this['reply_content'] !== null) this.form['reply_content'] = this['reply_content']
      console.log(this.form)
      this.submit();
    },
    /**
     * 上传文件
     * @param {Object} param文件参数
     */
    change_file(key_name) {
      var _self = this;
      // 选择图像方法
      uni.chooseFile({
        count: 1,
        sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album'], //从相册选择
        success: (file) => {
          const tempFilePaths = file.tempFilePaths;
          const filePath = tempFilePaths[0];
          uploadFilePathApi(
            '/online_consultation/upload',
            filePath,
            undefined,
            {
              i_want_to_customize: 'test',
            },
            (task) => {
              task.onProgressUpdate((res) => {
                this.percent = res.progress;
                console.log('上传进度' + res.progress);
                console.log('已经上传的数据长度' + res.totalBytesSent);
                console.log('预期需要上传的数据总长度' + res.totalBytesExpectedToSend);
              });
            }
          ).then((res) => {
            const filename = res.result.url;
            this.form[key_name] = filename;
          });
        },
        error: function (e) {
          console.log(e);
        },
      });
    },
    /**
     * 上传图片
     * @param {Object} param文件参数
     */
    change_img(key_name) {
      var _self = this;
      _self.upload_img_flag = false;
      // 选择图像方法
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album'], //从相册选择
        success: (file) => {
          const tempFilePaths = file.tempFilePaths;
          const filePath = tempFilePaths[0];
          uploadFilePathApi('/online_consultation/upload',filePath,undefined,undefined,
            (task) => {
              task.onProgressUpdate((res) => {
                this.percent = res.progress;
                console.log('上传进度' + res.progress);
                console.log('已经上传的数据长度' + res.totalBytesSent);
                console.log('预期需要上传的数据总长度' + res.totalBytesExpectedToSend);
              });
            }
          ).then((res) => {
            const filename = res.result.url;
            this.form[key_name] = filename;
          });
        },
        error: function (e) {
          console.log(e);
        },
      });
    },
    /**
     * 获取对象后获取缓存表单
     * @param {Object} json
     * @param {Object} func
     */
    get_obj_before(param) {
      var form = uni.db.get('form');
      if (form) {
        delete form.examine_state;
        delete form.examine_reply;
        this.obj = uni.push(this.obj, form);
        this.form = uni.push(this.form, form);
      }
      var arr = [];
      for (let key in form) {
        arr.push(key);
      }
      for (var i = 0; i < arr.length; i++) {
        this.disabledObj[arr[i] + '_isDisabled'] = true;
      }
                              uni.db.del('form');

      return param;
    },
        /**
     * 获取注册用户用户列表
     */
    async get_list_user_registered_user() {
      var json = await this.$get("~/api/user/get_list?user_group=注册用户");
      if(json.result && json.result.list){
        json.result.list.map((o) => this.list_user_registered_user.push({value:o.user_id,text:o.nickname + '-' + o.username}));
      }
      else if(json.error){
        console.error(json.error);
      }
    },
        /**
     * 获取注册用户用户组
     */
    async get_group_user_registered_user() {
      this.form["registered_user"] = this.user.user_id;
      var json = await this.$get("~/api/user_group/get_obj?name=注册用户");
      if(json.result && json.result.obj){
        this.group_user_registered_user = json.result.obj;
        this.get_user_session_registered_user(this.form['registered_user'])
      }
      else if(json.error){
        console.error(json.error);
      }
    },
    get_user_session_registered_user(id){
      var _this = this;
      var user_id = {"user_id":_this.user.user_id}
      var url = "~/api/"+_this.group_user_registered_user.source_table+"/get_obj"
      this.$get(url, user_id, function(res) {
        if (res.result && res.result.obj) {
          var arr = []
          for (let key in res.result.obj) {
            arr.push(key)
          }
          var arrForm = []
          for (let key in _this.form) {
            arrForm.push(key)
          }
          _this.form["registered_user"] = _this.user.user_id
          _this.disabledObj['registered_user' + '_isDisabled'] = true
          for (var i=0;i<arr.length;i++){
            if (arr[i]!=='examine_state' && arr[i]!=='examine_reply') {
              for (var j = 0; j < arrForm.length; j++) {
                if (arr[i] === arrForm[j]) {
                  if (arr[i] !== "registered_user") {
                    _this.form[arrForm[j]] = res.result.obj[arr[i]]
                    _this.disabledObj[arrForm[j] + '_isDisabled'] = true
                    break;
                  } else {
                    _this.disabledObj[arrForm[j] + '_isDisabled'] = true
                  }
                }
              }
            }
          }
        }
      });
    },
      
      
      
      
      
      
      /**
     * 获取对象之后
     * @param {Object} json
     * @param {Object} func
     */
    get_obj_after(json, func) {
                                                                            },

    is_view() {
      var bl = this.user_group == '管理员';

      if (!bl) {
        bl = this.$check_action('/online_consultation/table', 'add');
        console.log(bl ? '你有表格添加权限视作有添加权限' : '你没有表格添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/online_consultation/table', 'set');
        console.log(bl ? '你有表格添加权限视作有修改权限' : '你没有表格修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/online_consultation/view', 'add');
        console.log(bl ? '你有视图添加权限视作有添加权限' : '你没有视图添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/online_consultation/view', 'set');
        console.log(bl ? '你有视图修改权限视作有修改权限' : '你没有视图修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/online_consultation/view', 'get');
        console.log(bl ? '你有视图查询权限视作有查询权限' : '你没有视图查询权限');
      }

      console.log(
        bl
          ? '具有当前页面的查看权，请注意这不代表你有字段的查看权'
          : '无权查看当前页，请注意即便有字段查询权限没有页面查询权限也不行'
      );

      return bl;
    },
  },
  created() {
        this.get_list_user_registered_user();
          this.get_group_user_registered_user();
                              },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>