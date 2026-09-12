<template>
  <view class="page_diy_view page_hospital_information_view" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>医院信息详情</tn-nav-bar>
    <view id="page_diy_view" class="page-from">
      <uni-forms :modelValue="form" v-if="is_view()" labelWidth="70px">

        <uni-forms-item v-if="$check_field('get','hospital_name') || ($check_field('add','hospital_name') || $check_field('set','hospital_name'))" label="医院名称" name="hospital_name">
                      <uni-easyinput type="text" v-model="form['hospital_name']" v-if="(form['hospital_information_id'] && $check_field('set','hospital_name')) || (!form['hospital_information_id'] && $check_field('add','hospital_name'))" :disabled="disabledObj['hospital_name_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','hospital_name')">
            {{ form['hospital_name'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_address') || ($check_field('add','hospital_address') || $check_field('set','hospital_address'))" label="医院地址" name="hospital_address">
                      <uni-easyinput type="text" v-model="form['hospital_address']" v-if="(form['hospital_information_id'] && $check_field('set','hospital_address')) || (!form['hospital_information_id'] && $check_field('add','hospital_address'))" :disabled="disabledObj['hospital_address_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','hospital_address')">
            {{ form['hospital_address'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','type_of_hospital') || ($check_field('add','type_of_hospital') || $check_field('set','type_of_hospital'))" label="医院类型" name="type_of_hospital">
                      <uni-easyinput type="text" v-model="form['type_of_hospital']" v-if="(form['hospital_information_id'] && $check_field('set','type_of_hospital')) || (!form['hospital_information_id'] && $check_field('add','type_of_hospital'))" :disabled="disabledObj['type_of_hospital_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','type_of_hospital')">
            {{ form['type_of_hospital'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','distance_from_hospital') || ($check_field('add','distance_from_hospital') || $check_field('set','distance_from_hospital'))" label="医院距离" name="distance_from_hospital">
                      <uni-easyinput type="text" v-model="form['distance_from_hospital']" v-if="(form['hospital_information_id'] && $check_field('set','distance_from_hospital')) || (!form['hospital_information_id'] && $check_field('add','distance_from_hospital'))" :disabled="disabledObj['distance_from_hospital_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','distance_from_hospital')">
            {{ form['distance_from_hospital'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_phone_number') || ($check_field('add','hospital_phone_number') || $check_field('set','hospital_phone_number'))" label="医院电话" name="hospital_phone_number">
                      <uni-easyinput type="text" v-model="form['hospital_phone_number']" v-if="(form['hospital_information_id'] && $check_field('set','hospital_phone_number')) || (!form['hospital_information_id'] && $check_field('add','hospital_phone_number'))" :disabled="disabledObj['hospital_phone_number_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','hospital_phone_number')">
            {{ form['hospital_phone_number'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_score') || ($check_field('add','hospital_score') || $check_field('set','hospital_score'))" label="医院评分" name="hospital_score">
                      <uni-easyinput type="text" v-model="form['hospital_score']" v-if="(form['hospital_information_id'] && $check_field('set','hospital_score')) || (!form['hospital_information_id'] && $check_field('add','hospital_score'))" :disabled="disabledObj['hospital_score_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','hospital_score')">
            {{ form['hospital_score'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_pictures') || ($check_field('add','hospital_pictures') || $check_field('set','hospital_pictures'))" label="医院图片" name="hospital_pictures">
              <!-- 修改权限 -->
          <view class="from-img" v-if="form['hospital_pictures'] && $check_field('set', 'hospital_pictures')">
            <image v-if="disabledObj['hospital_pictures_isDisabled']" :src="$fullImgUrl(form['hospital_pictures'])" />
            <image v-if="!disabledObj['hospital_pictures_isDisabled']" :src="$fullImgUrl(form['hospital_pictures'])" @click="change_img('hospital_pictures')" />
          </view>
          <!-- 添加权限 -->
          <view class="from-img" v-else-if="!form['hospital_pictures'] && ($check_field('add','hospital_pictures') || $check_field('set','hospital_pictures'))">
            <view v-if="disabledObj['hospital_pictures_isDisabled']" class="add-img">
              <text>+</text>
            </view>
            <view v-if="!disabledObj['hospital_pictures_isDisabled']" class="add-img" @click="change_img('hospital_pictures')">
              <text>+</text>
            </view>
          </view>
          <!-- 查询权限 -->
          <view class="from-img" v-else-if="$check_field('get', 'hospital_pictures')">
            <image :src="$fullImgUrl(form['hospital_pictures'])" />
          </view>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','areas_of_expertise') || ($check_field('add','areas_of_expertise') || $check_field('set','areas_of_expertise'))" label="擅长领域" name="areas_of_expertise">
              <uni-easyinput type="textarea" v-model="form['areas_of_expertise']" v-if="(form['hospital_information_id'] && $check_field('set', 'areas_of_expertise')) || (!form['hospital_information_id'] && $check_field('add', 'areas_of_expertise'))" :disabled="disabledObj['areas_of_expertise_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'areas_of_expertise')">
            {{ form['areas_of_expertise'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_introduction') || ($check_field('add','hospital_introduction') || $check_field('set','hospital_introduction'))" label="医院介绍" name="hospital_introduction">
              <uni-easyinput type="textarea" v-model="form['hospital_introduction']" v-if="(form['hospital_information_id'] && $check_field('set', 'hospital_introduction')) || (!form['hospital_information_id'] && $check_field('add', 'hospital_introduction'))" :disabled="disabledObj['hospital_introduction_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'hospital_introduction')">
            {{ form['hospital_introduction'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','hospital_details') || ($check_field('add','hospital_details') || $check_field('set','hospital_details'))" label="医院详情" name="hospital_details">
              <uni-easyinput type="textarea" v-model="form['hospital_details']" v-if="(form['hospital_information_id'] && $check_field('set', 'hospital_details')) || (!form['hospital_information_id'] && $check_field('add', 'hospital_details'))" :disabled="disabledObj['hospital_details_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'hospital_details')">
            {{ form['hospital_details'] }}
          </text>
            </uni-forms-item>
      </uni-forms>
      <view class="form-footer" v-if="$check_action('/hospital_information/view','set') || ($check_action('/hospital_information/view','add') || $check_option('/hospital_information/table','examine'))">
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
      field: "hospital_information_id",
      url_add: "~/api/hospital_information/add?",
      url_set: "~/api/hospital_information/set?",
      url_get_obj: "~/api/hospital_information/get_obj?",
      url_upload: "~/api/hospital_information/upload?",
      hospital_name: null,
      hospital_address: null,
      type_of_hospital: null,
      distance_from_hospital: null,
      hospital_phone_number: null,
      hospital_score: null,
      hospital_pictures: null,
      areas_of_expertise: null,
      hospital_introduction: null,
      hospital_details: null,
      query: {
        "hospital_information_id": 0,
      },
      form: {
          "hospital_name":  '', // 医院名称
            "hospital_address":  '', // 医院地址
            "type_of_hospital":  '', // 医院类型
            "distance_from_hospital":  '', // 医院距离
            "hospital_phone_number":  '', // 医院电话
            "hospital_score":  '', // 医院评分
            "hospital_pictures":  '', // 医院图片
            "areas_of_expertise":  '', // 擅长领域
            "hospital_introduction":  '', // 医院介绍
            "hospital_details":  '', // 医院详情
          "hospital_information_id": 0, // ID
      },
      disabledObj:{
          "hospital_name_isDisabled": false,
            "hospital_address_isDisabled": false,
            "type_of_hospital_isDisabled": false,
            "distance_from_hospital_isDisabled": false,
            "hospital_phone_number_isDisabled": false,
            "hospital_score_isDisabled": false,
            "hospital_pictures_isDisabled": false,
            "areas_of_expertise_isDisabled": false,
            "hospital_introduction_isDisabled": false,
            "hospital_details_isDisabled": false,
        },
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
        url: this.$fullUrl('~/api/hospital_information/upload?'), //仅为示例，非真实的接口地址
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
      if (type == 'hospital_name') this['hospital_name'] = this.form['hospital_name'] = "";
      if (type == 'hospital_address') this['hospital_address'] = this.form['hospital_address'] = "";
      if (type == 'type_of_hospital') this['type_of_hospital'] = this.form['type_of_hospital'] = "";
      if (type == 'distance_from_hospital') this['distance_from_hospital'] = this.form['distance_from_hospital'] = "";
      if (type == 'hospital_phone_number') this['hospital_phone_number'] = this.form['hospital_phone_number'] = "";
      if (type == 'hospital_score') this['hospital_score'] = this.form['hospital_score'] = "";
      if (type == 'hospital_pictures') this['hospital_pictures'] = this.form['hospital_pictures'] = "";
      if (type == 'areas_of_expertise') this['areas_of_expertise'] = this.form['areas_of_expertise'] = "";
      if (type == 'hospital_introduction') this['hospital_introduction'] = this.form['hospital_introduction'] = "";
      if (type == 'hospital_details') this['hospital_details'] = this.form['hospital_details'] = "";
    },
    submit_() {
      if (this['hospital_name'] !== null) this.form['hospital_name'] = this['hospital_name']
      if (this['hospital_address'] !== null) this.form['hospital_address'] = this['hospital_address']
      if (this['type_of_hospital'] !== null) this.form['type_of_hospital'] = this['type_of_hospital']
      if (this['distance_from_hospital'] !== null) this.form['distance_from_hospital'] = this['distance_from_hospital']
      if (this['hospital_phone_number'] !== null) this.form['hospital_phone_number'] = this['hospital_phone_number']
      if (this['hospital_score'] !== null) this.form['hospital_score'] = this['hospital_score']
      if (this['hospital_pictures'] !== null) this.form['hospital_pictures'] = this['hospital_pictures']
      if (this['areas_of_expertise'] !== null) this.form['areas_of_expertise'] = this['areas_of_expertise']
      if (this['hospital_introduction'] !== null) this.form['hospital_introduction'] = this['hospital_introduction']
      if (this['hospital_details'] !== null) this.form['hospital_details'] = this['hospital_details']
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
            '/hospital_information/upload',
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
          uploadFilePathApi('/hospital_information/upload',filePath,undefined,undefined,
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
     * 获取对象之后
     * @param {Object} json
     * @param {Object} func
     */
    get_obj_after(json, func) {
                                                                                                                            },

    is_view() {
      var bl = this.user_group == '管理员';

      if (!bl) {
        bl = this.$check_action('/hospital_information/table', 'add');
        console.log(bl ? '你有表格添加权限视作有添加权限' : '你没有表格添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/hospital_information/table', 'set');
        console.log(bl ? '你有表格添加权限视作有修改权限' : '你没有表格修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/hospital_information/view', 'add');
        console.log(bl ? '你有视图添加权限视作有添加权限' : '你没有视图添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/hospital_information/view', 'set');
        console.log(bl ? '你有视图修改权限视作有修改权限' : '你没有视图修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/hospital_information/view', 'get');
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
                                          },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>