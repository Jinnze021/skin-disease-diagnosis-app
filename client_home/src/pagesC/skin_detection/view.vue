<template>
  <view class="page_diy_view page_skin_detection_view" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>皮肤检测详情</tn-nav-bar>
    <view id="page_diy_view" class="page-from">
      <uni-forms :modelValue="form" v-if="is_view()" labelWidth="70px">

        <uni-forms-item v-if="$check_field('get','registered_user') || ($check_field('add','registered_user') || $check_field('set','registered_user'))" label="注册用户" name="registered_user">
              <uni-data-select
              id="form_registered_user"
              v-model="form['registered_user']"
              :localdata="list_user_registered_user"
              :clear="!disabledObj['registered_user_isDisabled']"
              :disabled="disabledObj['registered_user_isDisabled']"
              v-if="(form['skin_detection_id'] && $check_field('set', 'registered_user')) || (!form['skin_detection_id'] && $check_field('add', 'registered_user'))"
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
                      <uni-easyinput type="text" v-model="form['user_name']" v-if="(form['skin_detection_id'] && $check_field('set','user_name')) || (!form['skin_detection_id'] && $check_field('add','user_name'))" :disabled="disabledObj['user_name_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','user_name')">
            {{ form['user_name'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','user_gender') || ($check_field('add','user_gender') || $check_field('set','user_gender'))" label="用户性别" name="user_gender">
                      <uni-easyinput type="text" v-model="form['user_gender']" v-if="(form['skin_detection_id'] && $check_field('set','user_gender')) || (!form['skin_detection_id'] && $check_field('add','user_gender'))" :disabled="disabledObj['user_gender_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','user_gender')">
            {{ form['user_gender'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','user_age') || ($check_field('add','user_age') || $check_field('set','user_age'))" label="用户年龄" name="user_age">
                      <uni-easyinput type="text" v-model="form['user_age']" v-if="(form['skin_detection_id'] && $check_field('set','user_age')) || (!form['skin_detection_id'] && $check_field('add','user_age'))" :disabled="disabledObj['user_age_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','user_age')">
            {{ form['user_age'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','entry_date') || ($check_field('add','entry_date') || $check_field('set','entry_date'))" label="录入日期" name="entry_date">
              <uni-datetime-picker v-if="(form['skin_detection_id'] && $check_field('set', 'entry_date')) || (!form['skin_detection_id'] && $check_field('add', 'entry_date'))" v-model="form['entry_date']" type="date" :disabled="disabledObj['entry_date_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'entry_date')">
            {{ form['entry_date'] }}
          </text>
	        </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','detection_picture') || ($check_field('add','detection_picture') || $check_field('set','detection_picture'))" label="检测图片" name="detection_picture">
              <!-- 修改权限 -->
          <view class="from-img" v-if="form['detection_picture'] && $check_field('set', 'detection_picture')">
            <image v-if="disabledObj['detection_picture_isDisabled']" :src="$fullImgUrl(form['detection_picture'])" />
            <image v-if="!disabledObj['detection_picture_isDisabled']" :src="$fullImgUrl(form['detection_picture'])" @click="change_img('detection_picture')" />
          </view>
          <!-- 添加权限 -->
          <view class="from-img" v-else-if="!form['detection_picture'] && ($check_field('add','detection_picture') || $check_field('set','detection_picture'))">
            <view v-if="disabledObj['detection_picture_isDisabled']" class="add-img">
              <text>+</text>
            </view>
            <view v-if="!disabledObj['detection_picture_isDisabled']" class="add-img" @click="change_img('detection_picture')">
              <text>+</text>
            </view>
          </view>
          <!-- 查询权限 -->
          <view class="from-img" v-else-if="$check_field('get', 'detection_picture')">
            <image :src="$fullImgUrl(form['detection_picture'])" />
          </view>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','skin_condition') || ($check_field('add','skin_condition') || $check_field('set','skin_condition'))" label="皮肤情况" name="skin_condition">
              <uni-easyinput type="textarea" v-model="form['skin_condition']" v-if="(form['skin_detection_id'] && $check_field('set', 'skin_condition')) || (!form['skin_detection_id'] && $check_field('add', 'skin_condition'))" :disabled="disabledObj['skin_condition_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'skin_condition')">
            {{ form['skin_condition'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','test_results') || ($check_field('add','test_results') || $check_field('set','test_results'))" label="检测结果" name="test_results">
                      <uni-easyinput type="text" v-model="form['test_results']" v-if="(form['skin_detection_id'] && $check_field('set','test_results')) || (!form['skin_detection_id'] && $check_field('add','test_results'))" :disabled="disabledObj['test_results_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','test_results')">
            {{ form['test_results'] }}
          </text>
                    </uni-forms-item>
      </uni-forms>
      <view class="form-footer" v-if="$check_action('/skin_detection/view','set') || ($check_action('/skin_detection/view','add') || $check_option('/skin_detection/table','examine'))">
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
      field: "skin_detection_id",
      url_add: "~/api/skin_detection/add?",
      url_set: "~/api/skin_detection/set?",
      url_get_obj: "~/api/skin_detection/get_obj?",
      url_upload: "~/api/skin_detection/upload?",
      registered_user: null,
      user_name: null,
      user_gender: null,
      user_age: null,
      entry_date: null,
      detection_picture: null,
      skin_condition: null,
      test_results: null,
      test_description: null,
      query: {
        "skin_detection_id": 0,
      },
      form: {
          "registered_user": 0, // 注册用户
            "user_name":  '', // 用户姓名
            "user_gender":  '', // 用户性别
            "user_age":  0 , // 用户年龄
            "entry_date": this.$toTime(new Date().getTime(), "yyyy-MM-dd"),
            "detection_picture":  '', // 检测图片
            "skin_condition":  '', // 皮肤情况
            "test_results":  '', // 检测结果
          "skin_detection_id": 0, // ID
      },
      disabledObj:{
          "registered_user_isDisabled": false,
            "user_name_isDisabled": false,
            "user_gender_isDisabled": false,
                "entry_date_isDisabled": false,
            "detection_picture_isDisabled": false,
            "skin_condition_isDisabled": false,
            "test_results_isDisabled": false,
            "test_description_isDisabled": false,
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
                                                                          if (!param.entry_date){
        return "录入日期不能为空";
      }
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
        url: this.$fullUrl('~/api/skin_detection/upload?'), //仅为示例，非真实的接口地址
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
      if (type == 'user_gender') this['user_gender'] = this.form['user_gender'] = "";
      if (type == 'user_age') this['user_age'] = this.form['user_age'] = "";
      if (type == 'entry_date') this['entry_date'] = this.form['entry_date'] = "";
      if (type == 'detection_picture') this['detection_picture'] = this.form['detection_picture'] = "";
      if (type == 'skin_condition') this['skin_condition'] = this.form['skin_condition'] = "";
      if (type == 'test_results') this['test_results'] = this.form['test_results'] = "";
      if (type == 'test_description') this['test_description'] = this.form['test_description'] = "";
    },
    submit_() {
      if (this['registered_user'] !== null) this.form['registered_user'] = this['registered_user']
      if (this['user_name'] !== null) this.form['user_name'] = this['user_name']
      if (this['user_gender'] !== null) this.form['user_gender'] = this['user_gender']
      if (this['user_age'] !== null) this.form['user_age'] = this['user_age']
      if (this['entry_date'] !== null) this.form['entry_date'] = this['entry_date']
      if (this['detection_picture'] !== null) this.form['detection_picture'] = this['detection_picture']
      if (this['skin_condition'] !== null) this.form['skin_condition'] = this['skin_condition']
      if (this['test_results'] !== null) this.form['test_results'] = this['test_results']
      if (this['test_description'] !== null) this.form['test_description'] = this['test_description']
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
            '/skin_detection/upload',
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
          uploadFilePathApi('/skin_detection/upload',filePath,undefined,undefined,
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
                          if (this.form["entry_date"] && JSON.stringify(this.form["entry_date"]).indexOf("-")===-1) {
        this.form["entry_date"] = this.$toTime(parseInt(this.form["entry_date"]), "yyyy-MM-dd")
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
                                                              if (this.form["entry_date"] && JSON.stringify(this.form["entry_date"]).indexOf("-")===-1) {
        this.form["entry_date"] = this.$toTime(parseInt(this.form["entry_date"]),"yyyy-MM-dd")
      }
                                                            },

    is_view() {
      var bl = this.user_group == '管理员';

      if (!bl) {
        bl = this.$check_action('/skin_detection/table', 'add');
        console.log(bl ? '你有表格添加权限视作有添加权限' : '你没有表格添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/skin_detection/table', 'set');
        console.log(bl ? '你有表格添加权限视作有修改权限' : '你没有表格修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/skin_detection/view', 'add');
        console.log(bl ? '你有视图添加权限视作有添加权限' : '你没有视图添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/skin_detection/view', 'set');
        console.log(bl ? '你有视图修改权限视作有修改权限' : '你没有视图修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/skin_detection/view', 'get');
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