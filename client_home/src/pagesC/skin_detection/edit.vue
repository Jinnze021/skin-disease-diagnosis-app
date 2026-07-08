<template>
  <view class="page_diy_edit page_skin_detection_edit" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>皮肤检测</tn-nav-bar>
    <view class="page-custom " id="page_diy_edit">
      <view class="custom-from">
        <!-- 新增诊断功能区域 -->
        <view class="diagnosis-section">
          <view class="diagnosis-header">AI智能诊断</view>
          
          <!-- 图片上传区域 -->
          <view class="upload-section" @click="handleEasyDLUpload">
            <image v-if="easyDLImage" :src="easyDLImage" class="preview-image" />
            <view v-else class="upload-placeholder">
              <uni-icons type="plusempty" size="40" color="#999"></uni-icons>
              <text class="upload-text">点击上传诊断图片</text>
            </view>
          </view>

          <!-- 诊断按钮 -->
          <button class="diagnosis-btn" :disabled="diagnosisLoading" @click="startDiagnosis">
            {{ diagnosisLoading ? '诊断中...' : '开始AI诊断' }}
          </button>

          <!-- 诊断结果 -->
          <view v-if="diagnosisResult" class="result-card">
            <view class="result-title">诊断结果</view>
            <view class="result-item">
              <text class="label">疾病名称：</text>
              <text class="value">{{ diagnosisResult.name }}</text>
            </view>
            <view class="result-item">
              <text class="label">置信度：</text>
              <text class="value">{{ (diagnosisResult.score * 100).toFixed(1) }}%</text>
            </view>
          </view>

          <!-- 错误提示 -->
          <view v-if="diagnosisError" class="error-message">
            <uni-icons type="info" size="16" color="#dd524d"></uni-icons>
            <text class="error-text">{{ diagnosisError }}</text>
          </view>
        </view>

        <!-- 现有表单内容 -->
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
              {{ form['registered_user'] }}
            </text>
          </view>
        </view>
        <view v-if="$check_field('set', 'user_name') || $check_field('add', 'user_name') ||  $check_field('get', 'user_name')" class="custom-from-item">
          <view class="label">
            <text>
              用户姓名
            </text>
          </view>
          <view class="content">
            <input type="text" id="form_user_name" v-model="form['user_name']" placeholder="请输入用户姓名" v-if="(form['user_name'] && $check_field('set','user_name')) || (!form['user_name'] && $check_field('add','user_name'))" :disabled="disabledObj['user_name_isDisabled']" />
            <text v-else-if="$check_field('get','user_name')">{{ form['user_name'] }}</text>
          </view>
        </view>
        <view v-if="$check_field('set', 'user_gender') || $check_field('add', 'user_gender') ||  $check_field('get', 'user_gender')" class="custom-from-item">
          <view class="label">
            <text>
              用户性别
            </text>
          </view>
          <view class="content">
            <input type="text" id="form_user_gender" v-model="form['user_gender']" placeholder="请输入用户性别" v-if="(form['user_gender'] && $check_field('set','user_gender')) || (!form['user_gender'] && $check_field('add','user_gender'))" :disabled="disabledObj['user_gender_isDisabled']" />
            <text v-else-if="$check_field('get','user_gender')">{{ form['user_gender'] }}</text>
          </view>
        </view>
        <view v-if="$check_field('set', 'user_age') || $check_field('add', 'user_age') ||  $check_field('get', 'user_age')" class="custom-from-item">
          <view class="label">
            <text>
              用户年龄
            </text>
          </view>
          <view class="content">
            <input type="text" id="form_user_age" v-model="form['user_age']" placeholder="请输入用户年龄" v-if="(form['user_age'] && $check_field('set','user_age')) || (!form['user_age'] && $check_field('add','user_age'))" :disabled="disabledObj['user_age_isDisabled']" />
            <text v-else-if="$check_field('get','user_age')">{{ form['user_age'] }}</text>
          </view>
        </view>
        <view v-if="$check_field('set', 'entry_date') || $check_field('add', 'entry_date') ||  $check_field('get', 'entry_date')" class="custom-from-item">
          <view class="label">
            <text>
              录入日期
            </text>
          </view>
          <view class="content">
            <uni-datetime-picker :disabled="disabledObj['entry_date_isDisabled']" type="date"
                                 id="form_entry_date" v-model="form['entry_date']" placeholder="请输入录入日期"
                                 v-if="(form['entry_date'] && $check_field('set', 'entry_date')) || (!form['entry_date'] && $check_field('add', 'entry_date'))" />
            <text v-else-if="$check_field('get', 'entry_date')">
              {{ form['entry_date'] }}
            </text>
          </view>
        </view>
        <view v-if="$check_field('set', 'detection_picture') || $check_field('add', 'detection_picture') ||  $check_field('get', 'detection_picture')" class="custom-from-item">
          <view class="label">
            <text>
              检测图片
            </text>
          </view>
          <view class="content" v-if="form['detection_picture'] && $check_field('set', 'detection_picture')">
            <image v-if="disabledObj['detection_picture_isDisabled']" :src="$fullImgUrl(form['detection_picture'])" />
            <image v-if="!disabledObj['detection_picture_isDisabled']" :src="$fullImgUrl(form['detection_picture'])" @click="change_img('detection_picture')" />
          </view>
          <view class="content" v-else-if="!form['detection_picture'] && $check_field('add', 'detection_picture')">
            <view v-if="disabledObj['detection_picture_isDisabled']" class="btn_add_img">
              <text>+</text>
            </view>
            <view v-if="!disabledObj['detection_picture_isDisabled']" class="btn_add_img diy_text_row" @click="change_img('detection_picture')">
              <text>+</text>
            </view>
          </view>
          <view class="content" v-else-if="$check_field('get', 'detection_picture')">
            <image :src="$fullImgUrl(form['detection_picture'])" />
          </view>
        </view>
        <view v-if="$check_field('set', 'skin_condition') || $check_field('add', 'skin_condition') ||  $check_field('get', 'skin_condition')" class="custom-from-item">
          <view class="label">
            <text>
              皮肤情况
            </text>
          </view>
          <view class="content">
            <textarea id="form_skin_condition" v-model="form['skin_condition']" v-if="(form['skin_condition'] && $check_field('set','skin_condition')) || (!form['skin_condition'] && $check_field('add','skin_condition'))" :disabled="disabledObj['skin_condition_isDisabled']"/>
            <text v-else-if="$check_field('get','skin_condition')">{{ form['skin_condition'] }}</text>
          </view>
        </view>
        <view v-if="$check_field('set', 'test_results') || $check_field('add', 'test_results') ||  $check_field('get', 'test_results')" class="custom-from-item">
          <view class="label">
            <text>
              检测结果
            </text>
          </view>
          <view class="content">
            <input type="text" id="form_test_results" v-model="form['test_results']" placeholder="请输入检测结果" v-if="(form['test_results'] && $check_field('set','test_results')) || (!form['test_results'] && $check_field('add','test_results'))" :disabled="disabledObj['test_results_isDisabled']" />
            <text v-else-if="$check_field('get','test_results')">{{ form['test_results'] }}</text>
          </view>
        </view>
        <view v-if="$check_field('set', 'test_description') || $check_field('add', 'test_description') ||  $check_field('get', 'test_description')" class="custom-from-item">
          <view class="label">
            <text>
              检测说明
            </text>
          </view>
          <view class="content">
            <textarea id="form_test_description" v-model="form['test_description']" v-if="(form['test_description'] && $check_field('set','test_description')) || (!form['test_description'] && $check_field('add','test_description'))" :disabled="disabledObj['test_description_isDisabled']"/>
            <text v-else-if="$check_field('get','test_description')">{{ form['test_description'] }}</text>
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

const API_KEY = '4TicJDr7LXxCVIwajYpu82Ew';
const SECRET_KEY = 'avYadETUcmbbAI8WnxQtwfpbLRTj0Y0W';
const EASYDL_URL = 'https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/archivetest';

export default {
  mixins: [mixin],
  data() {
    return {
      url_get_obj: '~/api/skin_detection/get_obj?',
      url_add: '~/api/skin_detection/add?',
      url_set: '~/api/skin_detection/set?',
      today: new Date().toISOString().split('T')[0],
      query: {
        skin_detection_id: 0,
        registered_user: 0,
        user_name: "",
        user_gender: "",
        user_age: 0,
        entry_date: "",
        detection_picture: "",
        skin_condition: "",
        test_results: "",
        test_description: "",
      },
      obj: {
        skin_detection_id: 0,
        registered_user: 0,
        user_name: '',
        user_gender: '',
        user_age: 0,
        entry_date: this.$toTime(new Date().getTime(), "yyyy-MM-dd"),
        detection_picture: '',
        skin_condition: '',
        test_results: '',
        test_description: '',
      },
      form: {
        skin_detection_id: 0,
        registered_user: 0,
        user_name: '',
        user_gender: '',
        user_age: 0,
        entry_date: this.$toTime(new Date().getTime(), "yyyy-MM-dd"),
        detection_picture: '',
        skin_condition: '',
        test_results: '',
        test_description: '',
      },
      disabledObj: {
        registered_user_isDisabled: false,
        user_name_isDisabled: false,
        user_gender_isDisabled: false,
        entry_date_isDisabled: false,
        detection_picture_isDisabled: false,
        skin_condition_isDisabled: false,
        test_results_isDisabled: false,
        test_description_isDisabled: false,
      },
      list_user_registered_user: [],
      field: 'skin_detection_id',
      table_key: 'skin_detection',

      // 新增诊断相关数据
      easyDLImage: '',      // 临时诊断图片
      diagnosisLoading: false,
      diagnosisResult: null,
      diagnosisError: null,
      accessToken: null,
      tokenExpireTime: 0
    };
  },
  methods: {
    async submit_check(param) {
      if (!param.entry_date) {
        return "录入日期不能为空";
      }
      return null;
    },

    async get_list_user_registered_user() {
      var json = await this.$get("~/api/user/get_list?user_group=注册用户");
      if (json.result && json.result.list) {
        json.result.list.map((o) => this.list_user_registered_user.push({ value: o.user_id, text: o.nickname + '-' + o.username }));
      } else if (json.error) {
        console.error(json.error);
      }
    },

    async get_user_session_registered_user() {
      var _this = this;
      var json = await this.$get("~/api/user_group/get_obj?name=注册用户");
      if (json.result && json.result.obj) {
        var source_table = json.result.obj.source_table;
        var user_id = _this.userInfo.user_id;
        if (user_id) {
          var url = "~/api/" + source_table + "/get_obj";
          this.$get(url, { user_id: _this.userInfo.user_id }, function (res) {
            if (res.result && res.result.obj) {
              var arr = [];
              for (let key in res.result.obj) {
                arr.push(key);
              }
              var arrForm = [];
              for (let key in _this.form) {
                arrForm.push(key);
              }
              _this.form["registered_user"] = user_id;
              _this.disabledObj['registered_user_isDisabled'] = true;
              for (var i = 0; i < arr.length; i++) {
                if (arr[i] !== 'examine_state' && arr[i] !== 'examine_reply') {
                  for (var j = 0; j < arrForm.length; j++) {
                    if (arr[i] === arrForm[j]) {
                      if (arr[i] !== "registered_user") {
                        _this.form[arrForm[j]] = res.result.obj[arr[i]];
                        _this.disabledObj[arrForm[j] + '_isDisabled'] = true;
                        break;
                      }
                    }
                  }
                }
              }
            }
          });
        }
      } else if (json.error) {
        console.error(json.error);
      }
    },

    change_img(key_name) {
      var _self = this;
      _self.upload_img_flag = false;
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'],
        sourceType: ['album'],
        success: function (res) {
          const tempFilePaths = res.tempFilePaths;
          const uploadTask = uni.uploadFile({
            url: _self.$fullUrl('~/api/skin_detection/upload?'),
            filePath: tempFilePaths[0],
            name: 'file',
            formData: {
              skin_detection: 'test',
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
      if (this.form["entry_date"] && JSON.stringify(this.form["entry_date"]).indexOf("-") === -1) {
        this.form["entry_date"] = this.$toTime(parseInt(this.form["entry_date"]), "yyyy-MM-dd");
      }
      uni.db.del('form');
      return param;
    },

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

    // 新增方法 - 处理EasyDL图片上传
    handleEasyDLUpload() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        success: async res => {
          const tempFile = res.tempFiles[0];
          this.easyDLImage = res.tempFilePaths[0];
          
          // 自动开始诊断（可选）
          // await this.startDiagnosis();
        }
      });
    },

    // 新增方法 - 获取Access Token
    async getAccessToken() {
      try {
        // 检查缓存有效性
        if (this.accessToken && Date.now() < this.tokenExpireTime) {
          return true;
        }

        const [err, res] = await uni.request({
          url: 'https://aip.baidubce.com/oauth/2.0/token',
          method: 'POST',
          header: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          data: {
            grant_type: 'client_credentials',
            client_id: API_KEY,
            client_secret: SECRET_KEY
          }
        });

        if (err || res.data.error) {
          throw new Error(res.data?.error_description || '获取访问令牌失败');
        }

        this.accessToken = res.data.access_token;
        this.tokenExpireTime = Date.now() + (res.data.expires_in - 300) * 1000; // 提前5分钟过期
        return true;
      } catch (error) {
        this.diagnosisError = error.message;
        return false;
      }
    },

    // 新增方法 - 转换图片为Base64
    async imageToBase64(filePath) {
      try {
        const [err, res] = await new Promise(resolve => {
          uni.getFileSystemManager().readFile({
            filePath,
            encoding: 'base64',
            success: res => resolve([null, res]),
            fail: err => resolve([err, null])
          });
        });

        if (err) throw new Error('图片转换失败');
        return `data:image/jpeg;base64,${res.data}`;
      } catch (error) {
        this.diagnosisError = error.message;
        return null;
      }
    },

    // 新增方法 - 开始诊断
    async startDiagnosis() {
      if (!this.easyDLImage) {
        this.diagnosisError = '请先上传诊断图片';
        return;
      }

      this.diagnosisLoading = true;
      this.diagnosisError = null;
      this.diagnosisResult = null;

      try {
        // 步骤1：获取Access Token
        if (!(await this.getAccessToken())) return;

        // 步骤2：转换图片
        const base64Data = await this.imageToBase64(this.easyDLImage);
        if (!base64Data) return;

        // 步骤3：调用EasyDL接口
        const [err, res] = await uni.request({
          url: `${EASYDL_URL}?access_token=${this.accessToken}`,
          method: 'POST',
          header: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          data: {
            image: base64Data.split(',')[1], // 去除前缀
            top_num: 1
          }
        });

        // 错误处理
        if (err) throw new Error('网络请求失败，请检查连接');
        if (res.data.error_code) this.handleApiError(res.data);
        if (!res.data.results?.length) throw new Error('未识别到有效结果');

        // 显示结果
        this.diagnosisResult = {
          name: res.data.results[0].name,
          score: res.data.results[0].score
        };

        // 可选：自动填充到表单
        this.form.test_results = this.diagnosisResult.name;
      } catch (error) {
        this.diagnosisError = error.message;
      } finally {
        this.diagnosisLoading = false;
      }
    },

    // 新增方法 - 处理API错误
    handleApiError(data) {
      const errorMap = {
        4: '服务超限额，请稍后重试',
        6: '无API访问权限',
        17: '每日调用量超限',
        18: 'QPS超限制',
        19: '总调用量超限',
        100: '无效参数',
        110: 'Access Token无效',
        111: 'Access Token过期'
      };
      throw new Error(errorMap[data.error_code] || data.error_msg);
    }
  },
  onLoad() {
    this.get_user_session_registered_user();
    this.get_list_user_registered_user();
  },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';

  .custom-from-item {
    margin-bottom: 20px;
  }

  .label {
    font-weight: bold;
    margin-bottom: 5px;
  }

  .content {
    display: flex;
    align-items: center;
  }

  .btn_add_img {
    width: 100px;
    height: 100px;
    border: 1px dashed #ccc;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 24px;
    color: #999;
  }

  .form-footer {
    margin-top: 20px;
    text-align: center;
  }

  .me-btn {
    padding: 10px 20px;
    background-color: #007aff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  /* 新增样式 */
  .diagnosis-section {
    margin: 30rpx;
    padding: 20rpx;
    background: #fff;
    border-radius: 16rpx;
    box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);

    .diagnosis-header {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }

    .upload-section {
      height: 300rpx;
      border: 2rpx dashed #ccc;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 30rpx;

      .preview-image {
        width: 100%;
        height: 100%;
        border-radius: 12rpx;
      }

      .upload-placeholder {
        text-align: center;
        .upload-text {
          display: block;
          color: #999;
          font-size: 28rpx;
          margin-top: 10rpx;
        }
      }
    }

    .diagnosis-btn {
      background: #2979ff;
      color: #fff;
      height: 80rpx;
      line-height: 80rpx;
      border-radius: 40rpx;
      font-size: 32rpx;

      &[disabled] {
        background: #a0c0ff;
      }
    }

    .result-card {
      margin-top: 30rpx;
      padding: 20rpx;
      background: #f8f9fa;
      border-radius: 12rpx;

      .result-title {
        font-size: 30rpx;
        font-weight: 500;
        color: #333;
        margin-bottom: 20rpx;
      }

      .result-item {
        display: flex;
        justify-content: space-between;
        padding: 10rpx 0;
        font-size: 28rpx;

        .label {
          color: #666;
        }

        .value {
          color: #333;
          font-weight: 500;
        }
      }
    }

    .error-message {
      margin-top: 20rpx;
      padding: 20rpx;
      background: #fee;
      border-radius: 8rpx;
      color: #dd524d;
      display: flex;
      align-items: center;

      .error-text {
        margin-left: 10rpx;
        font-size: 28rpx;
      }
    }
  }
</style>