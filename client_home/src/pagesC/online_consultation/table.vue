<template>
  <view class="page_diy_table page_online_consultation_table" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>在线咨询列表</tn-nav-bar>
    <view class="page-list" id="page_diy_table">
      <!-- 筛选模块(开始) -->
      <view class="search-wrap">
        <!-- 搜索栏 -->
        <uni-forms :modelValue="query" labelWidth="70px">
                <uni-forms-item label="咨询标题" name="advisory_title">
                <uni-easyinput type="text" v-model="query.advisory_title" placeholder="咨询标题" />
              </uni-forms-item>
                </uni-forms>
        <!-- /搜索栏 -->
        <view class="search-btn-wrap">
          <view class="me-btn btn-reset" @click="reset()"> 重置 </view>
          <view class="me-btn btn-search" @click="search_()"> 查询 </view>
        </view>
		<view class="toolbar">
				  <view width="100%" class="me-btn btn-add" @click="$navTo('/pagesC/online_consultation/view?')" v-if="$check_action('/online_consultation/table', 'add') || $check_action('/online_consultation/view', 'add')">添加</view>
				</view>
      </view>
      <!-- 列表 -->
      <view class="list-wrap-row">
        <view v-for="(o, i) in list" :key="i" class="list-item">
          <view class="item-row " v-if="0 && $check_field('get','registered_user')">
            <view class="label" v-if="true">
              <span>注册用户</span>
            </view>
              <view class="value">
              <span>{{ get_user_registered_user(o['registered_user']) }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','user_name')">
            <view class="label" v-if="true">
              <span>用户姓名</span>
            </view>
              <view class="value">
              <span>{{ o['user_name'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','advisory_title')">
            <view class="label" v-if="true">
              <span>咨询标题</span>
            </view>
              <view class="value">
              <span>{{ o['advisory_title'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','consulting_category')">
            <view class="label" v-if="true">
              <span>咨询类别</span>
            </view>
              <view class="value">
              <span>{{ o['consulting_category'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','consulting_content')">
            <view class="label" v-if="true">
              <span>咨询内容</span>
            </view>
              <view class="value">
              <span>{{ o['consulting_content'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','reply_content')">
            <view class="label" v-if="true">
              <span>回复内容</span>
            </view>
              <view class="value">
              <span>{{ o['reply_content'] }}</span>
            </view>
            </view>
          <view class="content">
          </view>
          <view class="operate-bar">
            <view width="100%" class="me-btn btn-delete" @click="delInfo(i)"
                  v-if="$check_action('/online_consultation/table', 'del') || $check_action('/online_consultation/view', 'del')">
              删除
            </view>
            <view width="100%" class="me-btn btn-info" @click="$navTo('/pagesC/online_consultation/view?' + field + '=' + o[field])"
                  v-if="$check_action('/online_consultation/table', 'set') || $check_action('/online_consultation/view', 'set') || $check_action('/online_consultation/view', 'get')">
              详情
            </view>
          </view>
        </view>
      </view>
      <!-- /列表 -->
      <!-- 分页器 -->
      <uni-pagination
        class="pager"
        show-icon="true"
        :total="count"
        :pageSize="query.size"
        :current="query.page"
        @change="page_change"
      ></uni-pagination>
      <!-- /分页器 -->
    </view>
  </view>
</template>
<script>
import mixin from '@/libs/mixins/page.js';

export default {
  mixins: [mixin],
  data() {
    return {
      // 获取数据地址
      url_get_list: '~/api/online_consultation/get_list?like=0',
      url_del: '~/api/online_consultation/del',

      // 字段ID
      field: 'online_consultation_id',

      // 查询
      query: {
        size: 7,
        page: 1,
                  advisory_title: "",
                    login_time: '',
        create_time: '',
      },

      // 数据
      list: [],
                  // 用户列表
      list_user_registered_user: [],
                          };
  },
  methods: {
    search_() {
      this.query.page = 1;
      this.get_list();
    },
    /**
     * 重置
     */
    reset: function reset() {
      uni.clear(this.query);
      uni.push(this.query, this.config);
      this.get_list();
    },
    get_list_before(param){
      var user_group = this.user.user_group;
      if(user_group != "管理员"){
          let sqlwhere = "(";
                  if(user_group=="注册用户"){
          sqlwhere+= "registered_user = " + this.user.user_id + " or ";
        }
                                                                    if (sqlwhere.length>1){
          sqlwhere = sqlwhere.substr(0,sqlwhere.length-4);
          sqlwhere += ")";
          param["sqlwhere"] = sqlwhere;
        }
        }
      return param;
    },
    delInfo(v) {
      let _this = this;
      uni.showModal({
        title: '删除',
        content: '此操作将永久删除该文件, 是否继续?',
        success: function (res) {
          if (res.confirm) {
            let list = [v];
            _this.delAll(list);
          } else if (res.cancel) {
            console.log('用户点击取消');
          }
        },
      });
    },
          /**
     * 获取注册用户用户列表
     */
    async get_list_user_registered_user() {
      var json = await this.$get("~/api/user/get_list?user_group=注册用户");
      if(json.result && json.result.list){
        this.list_user_registered_user = json.result.list;
      }
      else if(json.error){
        console.error(json.error);
      }
    },

    get_user_registered_user(id){
      let obj = this.list_user_registered_user;
      let ret = "";
      for(let i=0;i<obj.length;i++){
        if(obj[i].user_id==id){
          ret = obj[i].nickname+"-"+obj[i].username;
        }
      }
      return ret;
    },
                                  },
  created() {
            this.get_list_user_registered_user();
                        },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>