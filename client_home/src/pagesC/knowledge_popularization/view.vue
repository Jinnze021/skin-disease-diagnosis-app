<template>
  <view class="page_diy_view page_knowledge_popularization_view" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>知识科普详情</tn-nav-bar>
    <view id="page_diy_view" class="page-from">
      <uni-forms :modelValue="form" v-if="is_view()" labelWidth="70px">

        <uni-forms-item v-if="$check_field('get','name_of_skin_disease') || ($check_field('add','name_of_skin_disease') || $check_field('set','name_of_skin_disease'))" label="皮肤病名称" name="name_of_skin_disease">
                      <uni-easyinput type="text" v-model="form['name_of_skin_disease']" v-if="(form['knowledge_popularization_id'] && $check_field('set','name_of_skin_disease')) || (!form['knowledge_popularization_id'] && $check_field('add','name_of_skin_disease'))" :disabled="disabledObj['name_of_skin_disease_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','name_of_skin_disease')">
            {{ form['name_of_skin_disease'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','types_of_skin_diseases') || ($check_field('add','types_of_skin_diseases') || $check_field('set','types_of_skin_diseases'))" label="皮肤病种类" name="types_of_skin_diseases">
                      <uni-easyinput type="text" v-model="form['types_of_skin_diseases']" v-if="(form['knowledge_popularization_id'] && $check_field('set','types_of_skin_diseases')) || (!form['knowledge_popularization_id'] && $check_field('add','types_of_skin_diseases'))" :disabled="disabledObj['types_of_skin_diseases_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','types_of_skin_diseases')">
            {{ form['types_of_skin_diseases'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','release_date') || ($check_field('add','release_date') || $check_field('set','release_date'))" label="发布日期" name="release_date">
              <uni-datetime-picker v-if="(form['knowledge_popularization_id'] && $check_field('set', 'release_date')) || (!form['knowledge_popularization_id'] && $check_field('add', 'release_date'))" v-model="form['release_date']" type="date" :disabled="disabledObj['release_date_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'release_date')">
            {{ form['release_date'] }}
          </text>
	        </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','treatment_mode') || ($check_field('add','treatment_mode') || $check_field('set','treatment_mode'))" label="治疗方式" name="treatment_mode">
                      <uni-easyinput type="text" v-model="form['treatment_mode']" v-if="(form['knowledge_popularization_id'] && $check_field('set','treatment_mode')) || (!form['knowledge_popularization_id'] && $check_field('add','treatment_mode'))" :disabled="disabledObj['treatment_mode_isDisabled']" />
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get','treatment_mode')">
            {{ form['treatment_mode'] }}
          </text>
                    </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','popular_science_pictures') || ($check_field('add','popular_science_pictures') || $check_field('set','popular_science_pictures'))" label="科普图片" name="popular_science_pictures">
              <!-- 修改权限 -->
          <view class="from-img" v-if="form['popular_science_pictures'] && $check_field('set', 'popular_science_pictures')">
            <image v-if="disabledObj['popular_science_pictures_isDisabled']" :src="$fullImgUrl(form['popular_science_pictures'])" />
            <image v-if="!disabledObj['popular_science_pictures_isDisabled']" :src="$fullImgUrl(form['popular_science_pictures'])" @click="change_img('popular_science_pictures')" />
          </view>
          <!-- 添加权限 -->
          <view class="from-img" v-else-if="!form['popular_science_pictures'] && ($check_field('add','popular_science_pictures') || $check_field('set','popular_science_pictures'))">
            <view v-if="disabledObj['popular_science_pictures_isDisabled']" class="add-img">
              <text>+</text>
            </view>
            <view v-if="!disabledObj['popular_science_pictures_isDisabled']" class="add-img" @click="change_img('popular_science_pictures')">
              <text>+</text>
            </view>
          </view>
          <!-- 查询权限 -->
          <view class="from-img" v-else-if="$check_field('get', 'popular_science_pictures')">
            <image :src="$fullImgUrl(form['popular_science_pictures'])" />
          </view>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','popular_science_video') || ($check_field('add','popular_science_video') || $check_field('set','popular_science_video'))" label="科普视频" name="popular_science_video">
              <view class="field-value">
            <!-- 查询权限 -->
            <view class="diy_video" v-if="!popular_science_video && $check_field('get', 'popular_science_video') && form['popular_science_video']">
              <view class="me-btn" style="margin-bottom: 8rpx" @click="close_('popular_science_video')">移除</view>
              <video style="width: 100%" :src="$fullImgUrl(form['popular_science_video'])" controls></video>
            </view>
            <view v-else-if="$check_field('add', 'popular_science_video') || $check_field('set', 'popular_science_video')">
              <view class="me-btn" @click="uploadFile_('popular_science_video')"> 上传视频 </view>
            </view>
            <view class="file-url" v-if="popular_science_video">{{ popular_science_video }}</view>
          </view>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','skin_disease_introduction') || ($check_field('add','skin_disease_introduction') || $check_field('set','skin_disease_introduction'))" label="皮肤病介绍" name="skin_disease_introduction">
              <uni-easyinput type="textarea" v-model="form['skin_disease_introduction']" v-if="(form['knowledge_popularization_id'] && $check_field('set', 'skin_disease_introduction')) || (!form['knowledge_popularization_id'] && $check_field('add', 'skin_disease_introduction'))" :disabled="disabledObj['skin_disease_introduction_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'skin_disease_introduction')">
            {{ form['skin_disease_introduction'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','treatment_plan') || ($check_field('add','treatment_plan') || $check_field('set','treatment_plan'))" label="治疗方案" name="treatment_plan">
              <uni-easyinput type="textarea" v-model="form['treatment_plan']" v-if="(form['knowledge_popularization_id'] && $check_field('set', 'treatment_plan')) || (!form['knowledge_popularization_id'] && $check_field('add', 'treatment_plan'))" :disabled="disabledObj['treatment_plan_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'treatment_plan')">
            {{ form['treatment_plan'] }}
          </text>
            </uni-forms-item>
        <uni-forms-item v-if="$check_field('get','science_popularization_details') || ($check_field('add','science_popularization_details') || $check_field('set','science_popularization_details'))" label="科普详情" name="science_popularization_details">
              <uni-easyinput type="textarea" v-model="form['science_popularization_details']" v-if="(form['knowledge_popularization_id'] && $check_field('set', 'science_popularization_details')) || (!form['knowledge_popularization_id'] && $check_field('add', 'science_popularization_details'))" :disabled="disabledObj['science_popularization_details_isDisabled']"/>
          <!-- 仅查看 -->
          <text v-else-if="$check_field('get', 'science_popularization_details')">
            {{ form['science_popularization_details'] }}
          </text>
            </uni-forms-item>
      </uni-forms>
      <view class="form-footer" v-if="$check_action('/knowledge_popularization/view','set') || ($check_action('/knowledge_popularization/view','add') || $check_option('/knowledge_popularization/table','examine'))">
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
      field: "knowledge_popularization_id",
      url_add: "~/api/knowledge_popularization/add?",
      url_set: "~/api/knowledge_popularization/set?",
      url_get_obj: "~/api/knowledge_popularization/get_obj?",
      url_upload: "~/api/knowledge_popularization/upload?",
      name_of_skin_disease: null,
      types_of_skin_diseases: null,
      release_date: null,
      treatment_mode: null,
      popular_science_pictures: null,
      popular_science_video: null,
      skin_disease_introduction: null,
      treatment_plan: null,
      science_popularization_details: null,
      query: {
        "knowledge_popularization_id": 0,
      },
      form: {
          "name_of_skin_disease":  '', // 皮肤病名称
            "types_of_skin_diseases":  '', // 皮肤病种类
            "release_date": this.$toTime(new Date().getTime(), "yyyy-MM-dd"),
            "treatment_mode":  '', // 治疗方式
            "popular_science_pictures":  '', // 科普图片
            "popular_science_video":  '', // 科普视频
            "skin_disease_introduction":  '', // 皮肤病介绍
            "treatment_plan":  '', // 治疗方案
            "science_popularization_details":  '', // 科普详情
          "knowledge_popularization_id": 0, // ID
      },
      disabledObj:{
          "name_of_skin_disease_isDisabled": false,
            "types_of_skin_diseases_isDisabled": false,
            "release_date_isDisabled": false,
            "treatment_mode_isDisabled": false,
            "popular_science_pictures_isDisabled": false,
            "popular_science_video_isDisabled": false,
            "skin_disease_introduction_isDisabled": false,
            "treatment_plan_isDisabled": false,
            "science_popularization_details_isDisabled": false,
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
                                          if (!param.release_date){
        return "发布日期不能为空";
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
        url: this.$fullUrl('~/api/knowledge_popularization/upload?'), //仅为示例，非真实的接口地址
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
      if (type == 'name_of_skin_disease') this['name_of_skin_disease'] = this.form['name_of_skin_disease'] = "";
      if (type == 'types_of_skin_diseases') this['types_of_skin_diseases'] = this.form['types_of_skin_diseases'] = "";
      if (type == 'release_date') this['release_date'] = this.form['release_date'] = "";
      if (type == 'treatment_mode') this['treatment_mode'] = this.form['treatment_mode'] = "";
      if (type == 'popular_science_pictures') this['popular_science_pictures'] = this.form['popular_science_pictures'] = "";
      if (type == 'popular_science_video') this['popular_science_video'] = this.form['popular_science_video'] = "";
      if (type == 'skin_disease_introduction') this['skin_disease_introduction'] = this.form['skin_disease_introduction'] = "";
      if (type == 'treatment_plan') this['treatment_plan'] = this.form['treatment_plan'] = "";
      if (type == 'science_popularization_details') this['science_popularization_details'] = this.form['science_popularization_details'] = "";
    },
    submit_() {
      if (this['name_of_skin_disease'] !== null) this.form['name_of_skin_disease'] = this['name_of_skin_disease']
      if (this['types_of_skin_diseases'] !== null) this.form['types_of_skin_diseases'] = this['types_of_skin_diseases']
      if (this['release_date'] !== null) this.form['release_date'] = this['release_date']
      if (this['treatment_mode'] !== null) this.form['treatment_mode'] = this['treatment_mode']
      if (this['popular_science_pictures'] !== null) this.form['popular_science_pictures'] = this['popular_science_pictures']
      if (this['popular_science_video'] !== null) this.form['popular_science_video'] = this['popular_science_video']
      if (this['skin_disease_introduction'] !== null) this.form['skin_disease_introduction'] = this['skin_disease_introduction']
      if (this['treatment_plan'] !== null) this.form['treatment_plan'] = this['treatment_plan']
      if (this['science_popularization_details'] !== null) this.form['science_popularization_details'] = this['science_popularization_details']
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
            '/knowledge_popularization/upload',
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
          uploadFilePathApi('/knowledge_popularization/upload',filePath,undefined,undefined,
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
                  if (this.form["release_date"] && JSON.stringify(this.form["release_date"]).indexOf("-")===-1) {
        this.form["release_date"] = this.$toTime(parseInt(this.form["release_date"]), "yyyy-MM-dd")
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
                                      if (this.form["release_date"] && JSON.stringify(this.form["release_date"]).indexOf("-")===-1) {
        this.form["release_date"] = this.$toTime(parseInt(this.form["release_date"]),"yyyy-MM-dd")
      }
                                                                                    },

    is_view() {
      var bl = this.user_group == '管理员';

      if (!bl) {
        bl = this.$check_action('/knowledge_popularization/table', 'add');
        console.log(bl ? '你有表格添加权限视作有添加权限' : '你没有表格添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/knowledge_popularization/table', 'set');
        console.log(bl ? '你有表格添加权限视作有修改权限' : '你没有表格修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/knowledge_popularization/view', 'add');
        console.log(bl ? '你有视图添加权限视作有添加权限' : '你没有视图添加权限');
      }
      if (!bl) {
        bl = this.$check_action('/knowledge_popularization/view', 'set');
        console.log(bl ? '你有视图修改权限视作有修改权限' : '你没有视图修改权限');
      }
      if (!bl) {
        bl = this.$check_action('/knowledge_popularization/view', 'get');
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