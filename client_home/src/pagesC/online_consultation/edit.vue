<template>
  <view class="page_diy_edit page_online_consultation_edit" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>在线咨询</tn-nav-bar>
    <view class="page-custom " id="page_diy_edit">
      <view class="custom-from">
          <view v-if="$check_field('set', 'registered_user') || $check_field('add', 'registered_user') ||  $check_field('get', 'registered_user')" class="custom-from-item">
          <view class="label">
            <text>注册用户</text>
          </view>
          <view class="content">
            <uni-data-select
                    id="form_registered_user" v-model="form['registered_user']"
                    :localdata="list_user_registered_user"
                    :clear="!disabledObj['registered_user_isDisabled']"
                    :disabled="disabledObj['registered_user_isDisabled']"
                    v-if="(form['registered_user'] && $check_field('set', 'registered_user')) || (!form['registered_user'] && $check_field('add', 'registered_user'))" style="width: 100%;">
            </uni-data-select>
            <text v-else-if="$check_field('get', 'registered_user')">
              {{form['registered_user']}}
            </text>
          </view>
        </view>
            <view v-if="$check_field('set', 'user_name') || $check_field('add', 'user_name') ||  $check_field('get', 'user_name')" class="custom-from-item">
          <view class="label">
            <text>
              用户姓名
            </text>
          </view>
              <!-- 文本 -->
                  <view class="content">
            <input type="text" id="form_user_name" v-model="form['user_name']" placeholder="请输入用户姓名" v-if="(form['user_name'] && $check_field('set','user_name')) || (!form['user_name'] && $check_field('add','user_name'))" :disabled="disabledObj['user_name_isDisabled']" />
            <text v-else-if="$check_field('get','user_name')">{{ form['user_name'] }}</text>
          </view>
                    </view>
            <view v-if="$check_field('set', 'advisory_title') || $check_field('add', 'advisory_title') ||  $check_field('get', 'advisory_title')" class="custom-from-item">
          <view class="label">
            <text>
              咨询标题
            </text>
          </view>
              <!-- 文本 -->
                  <view class="content">
            <input type="text" id="form_advisory_title" v-model="form['advisory_title']" placeholder="请输入咨询标题" v-if="(form['advisory_title'] && $check_field('set','advisory_title')) || (!form['advisory_title'] && $check_field('add','advisory_title'))" :disabled="disabledObj['advisory_title_isDisabled']" />
            <text v-else-if="$check_field('get','advisory_title')">{{ form['advisory_title'] }}</text>
          </view>
                    </view>
            <view v-if="$check_field('set', 'consulting_category') || $check_field('add', 'consulting_category') ||  $check_field('get', 'consulting_category')" class="custom-from-item">
          <view class="label">
            <text>
              咨询类别
            </text>
          </view>
              <!-- 文本 -->
                  <view class="content">
            <input type="text" id="form_consulting_category" v-model="form['consulting_category']" placeholder="请输入咨询类别" v-if="(form['consulting_category'] && $check_field('set','consulting_category')) || (!form['consulting_category'] && $check_field('add','consulting_category'))" :disabled="disabledObj['consulting_category_isDisabled']" />
            <text v-else-if="$check_field('get','consulting_category')">{{ form['consulting_category'] }}</text>
          </view>
                    </view>
            <view v-if="$check_field('set', 'consulting_content') || $check_field('add', 'consulting_content') ||  $check_field('get', 'consulting_content')" class="custom-from-item">
          <view class="label">
            <text>
              咨询内容
            </text>
          </view>
              <!-- 多文本 -->
          <view class="content">
            <textarea id="form_consulting_content" v-model="form['consulting_content']" v-if="(form['consulting_content'] && $check_field('set','consulting_content')) || (!form['consulting_content'] && $check_field('add','consulting_content'))" :disabled="disabledObj['consulting_content_isDisabled']"/>
            <text v-else-if="$check_field('get','consulting_content')">{{ form['consulting_content'] }}</text>
          </view>
            </view>
            <view v-if="$check_field('set', 'reply_content') || $check_field('add', 'reply_content') ||  $check_field('get', 'reply_content')" class="custom-from-item">
          <view class="label">
            <text>
              回复内容
            </text>
          </view>
              <!-- 多文本 -->
          <view class="content">
            <textarea id="form_reply_content" v-model="form['reply_content']" v-if="(form['reply_content'] && $check_field('set','reply_content')) || (!form['reply_content'] && $check_field('add','reply_content'))" :disabled="disabledObj['reply_content_isDisabled']"/>
            <text v-else-if="$check_field('get','reply_content')">{{ form['reply_content'] }}</text>
          </view>
            </view>
          <view class="form-footer">
          <view class="me-btn" @click="submit()">提交</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import mixin from '@/libs/mixins/page.js';
export default {
  mixins: [mixin],
  data() {
    return {
      url_get_obj: '~/api/online_consultation/get_obj?',
      url_add: '~/api/online_consultation/add?',
      url_set: '~/api/online_consultation/set?',
	  today: new Date().toISOString().split('T')[0],
      // 查询条件
      query: {
        online_consultation_id: 0,
          registered_user: 0,
            user_name: "",
            advisory_title: "",
            consulting_category: "",
            consulting_content: "",
            reply_content: "",
        },
      obj: {
        online_consultation_id: 0,
          registered_user: 0, // 注册用户
            user_name:  '', // 用户姓名
            advisory_title:  '', // 咨询标题
            consulting_category:  '', // 咨询类别
            consulting_content:  '', // 咨询内容
            reply_content:  '', // 回复内容
        },

      // 表单字段
      form: {
        online_consultation_id: 0,
          registered_user: 0, // 注册用户
            user_name:  '', // 用户姓名
            advisory_title:  '', // 咨询标题
            consulting_category:  '', // 咨询类别
            consulting_content:  '', // 咨询内容
            reply_content:  '', // 回复内容
        },
      disabledObj: {
          registered_user_isDisabled: false,
            user_name_isDisabled: false,
            advisory_title_isDisabled: false,
            consulting_category_isDisabled: false,
            consulting_content_isDisabled: false,
            reply_content_isDisabled: false,
        },
              // 用户列表
      list_user_registered_user: [],
                                                  field: 'online_consultation_id',
      table_key: 'online_consultation',

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
            async get_user_session_registered_user(){
        var _this = this;
        var json = await this.$get("~/api/user_group/get_obj?name=注册用户");
        if(json.result && json.result.obj){
          var source_table = json.result.obj.source_table;
          var user_id = _this.userInfo.user_id;
          if (user_id){
            var url = "~/api/"+source_table+"/get_obj"
            this.$get(url, { user_id: _this.userInfo.user_id }, function (res) {
              if (res.result && res.result.obj) {
                var arr = []
                for (let key in res.result.obj) {
                  arr.push(key)
                }
                var arrForm = []
                for (let key in _this.form) {
                  arrForm.push(key)
                }
                _this.form["registered_user"] = user_id
                _this.disabledObj['registered_user' + '_isDisabled'] = true
                for (var i=0;i<arr.length;i++){
                  if (arr[i]!=='examine_state' && arr[i]!=='examine_reply') {
                    for (var j = 0; j < arrForm.length; j++) {
                      if (arr[i] === arrForm[j]) {
                        if (arr[i] !== "registered_user") {
                          _this.form[arrForm[j]] = res.result.obj[arr[i]]
                          _this.disabledObj[arrForm[j] + '_isDisabled'] = true
                          break;
                        }
                      }
                    }
                  }
                }
              }
            });
          }
        }
        else if(json.error){
          console.error(json.error);
        }
    },
            
                
                
                
                
                change_img(key_name) {
      var _self = this;
      _self.upload_img_flag = false;
      // 选择图像方法
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album'], //从相册选择
        success: function (res) {
          const tempFilePaths = res.tempFilePaths;
          const uploadTask = uni.uploadFile({
            url: _self.$fullUrl('~/api/online_consultation/upload?'),
            filePath: tempFilePaths[0],
            name: 'file',
            formData: {
              online_consultation: 'test',
            },
            header: {
              'x-auth-token': _self.$store.state.app.userInfo.token,
            },
            success: function (uploadFileRes) {
              var filename = JSON.parse(uploadFileRes.data).result.url;
              var img_url = filename;
              _self.form[key_name] = img_url;
            },
          });

          uploadTask.onProgressUpdate(function (res) {
            _self.percent = res.progress;
            console.log('上传进度' + res.progress);
            console.log('已经上传的数据长度' + res.totalBytesSent);
            console.log('预期需要上传的数据总长度' + res.totalBytesExpectedToSend);
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
     * 获取对象后获取缓存表单
     * @param {Object} json
     * @param {Object} func
     */
    get_obj_after(json, func) {
      var form = uni.db.get('form');
      var obj = Object.assign({}, form, this.obj);
      if (form) {
        this.obj = uni.push(this.obj, obj);
      }
      if (form) {
        this.form = uni.push(this.form, form);
      }
      if (func) {
        func(json);
      }
    },

  },
  onLoad() {
        this.get_user_session_registered_user();
        this.get_list_user_registered_user();
                                  },
};
</script>

