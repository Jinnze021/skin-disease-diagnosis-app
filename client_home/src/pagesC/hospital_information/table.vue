<template>
  <view class="page_diy_table page_hospital_information_table" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>医院信息列表</tn-nav-bar>
    <view class="page-list" id="page_diy_table">
      <!-- 筛选模块(开始) -->
      <view class="search-wrap">
        <!-- 搜索栏 -->
        <uni-forms :modelValue="query" labelWidth="70px">
            <uni-forms-item label="医院名称" name="hospital_name">
                <uni-easyinput type="text" v-model="query.hospital_name" placeholder="医院名称" />
              </uni-forms-item>
              <uni-forms-item label="医院地址" name="hospital_address">
                <uni-easyinput type="text" v-model="query.hospital_address" placeholder="医院地址" />
              </uni-forms-item>
                          </uni-forms>
        <!-- /搜索栏 -->
        <view class="search-btn-wrap">
          <view class="me-btn btn-reset" @click="reset()"> 重置 </view>
          <view class="me-btn btn-search" @click="search_()"> 查询 </view>
        </view>
		<view class="toolbar">
				  <view width="100%" class="me-btn btn-add" @click="$navTo('/pagesC/hospital_information/view?')" v-if="$check_action('/hospital_information/table', 'add') || $check_action('/hospital_information/view', 'add')">添加</view>
				</view>
      </view>
      <!-- 列表 -->
      <view class="list-wrap-row">
        <view v-for="(o, i) in list" :key="i" class="list-item">
          <view class="item-row " v-if="1 && $check_field('get','hospital_name')">
            <view class="label" v-if="true">
              <span>医院名称</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_name'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','hospital_address')">
            <view class="label" v-if="true">
              <span>医院地址</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_address'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','type_of_hospital')">
            <view class="label" v-if="true">
              <span>医院类型</span>
            </view>
              <view class="value">
              <span>{{ o['type_of_hospital'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','distance_from_hospital')">
            <view class="label" v-if="true">
              <span>医院距离</span>
            </view>
              <view class="value">
              <span>{{ o['distance_from_hospital'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','hospital_phone_number')">
            <view class="label" v-if="true">
              <span>医院电话</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_phone_number'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','hospital_score')">
            <view class="label" v-if="true">
              <span>医院评分</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_score'] }}</span>
            </view>
            </view>
          <view class="item-row diy-image" v-if="1 && $check_field('get','hospital_pictures')">
            <view class="label" v-if=" false">
              <span>医院图片</span>
            </view>
              <view class="value diy_img">
              <image :src="$fullImgUrl(o['hospital_pictures'])" width="100%" height="100" />
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','areas_of_expertise')">
            <view class="label" v-if="true">
              <span>擅长领域</span>
            </view>
              <view class="value">
              <span>{{ o['areas_of_expertise'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','hospital_introduction')">
            <view class="label" v-if="true">
              <span>医院介绍</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_introduction'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','hospital_details')">
            <view class="label" v-if="true">
              <span>医院详情</span>
            </view>
              <view class="value">
              <span>{{ o['hospital_details'] }}</span>
            </view>
            </view>
          <view class="content">
            <view class="item-row">
              <view class="value">点赞数{{ o['praise_len'] }}</view>
            </view>
            <view class="item-row">
              <view class="value">点击数{{ o['hits'] }}</view>
            </view>
          </view>
          <view class="operate-bar">
            <view width="100%" class="me-btn btn-delete" @click="delInfo(i)"
                  v-if="$check_action('/hospital_information/table', 'del') || $check_action('/hospital_information/view', 'del')">
              删除
            </view>
            <view width="100%" class="me-btn btn-info" @click="$navTo('/pagesC/hospital_information/view?' + field + '=' + o[field])"
                  v-if="$check_action('/hospital_information/table', 'set') || $check_action('/hospital_information/view', 'set') || $check_action('/hospital_information/view', 'get')">
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
      url_get_list: '~/api/hospital_information/get_list?like=0',
      url_del: '~/api/hospital_information/del',

      // 字段ID
      field: 'hospital_information_id',

      // 查询
      query: {
        size: 7,
        page: 1,
              hospital_name: "",
                    hospital_address: "",
                              login_time: '',
        create_time: '',
      },

      // 数据
      list: [],
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
                                                              },
  created() {
                                          },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>