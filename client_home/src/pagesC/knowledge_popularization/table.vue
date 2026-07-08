<template>
  <view class="page_diy_table page_knowledge_popularization_table" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>知识科普列表</tn-nav-bar>
    <view class="page-list" id="page_diy_table">
      <!-- 筛选模块(开始) -->
      <view class="search-wrap">
        <!-- 搜索栏 -->
        <uni-forms :modelValue="query" labelWidth="70px">
            <uni-forms-item label="皮肤病名称" name="name_of_skin_disease">
                <uni-easyinput type="text" v-model="query.name_of_skin_disease" placeholder="皮肤病名称" />
              </uni-forms-item>
              <uni-forms-item label="皮肤病种类" name="types_of_skin_diseases">
                <uni-easyinput type="text" v-model="query.types_of_skin_diseases" placeholder="皮肤病种类" />
              </uni-forms-item>
                        </uni-forms>
        <!-- /搜索栏 -->
        <view class="search-btn-wrap">
          <view class="me-btn btn-reset" @click="reset()"> 重置 </view>
          <view class="me-btn btn-search" @click="search_()"> 查询 </view>
        </view>
		<view class="toolbar">
				  <view width="100%" class="me-btn btn-add" @click="$navTo('/pagesC/knowledge_popularization/view?')" v-if="$check_action('/knowledge_popularization/table', 'add') || $check_action('/knowledge_popularization/view', 'add')">添加</view>
				</view>
      </view>
      <!-- 列表 -->
      <view class="list-wrap-row">
        <view v-for="(o, i) in list" :key="i" class="list-item">
          <view class="item-row " v-if="1 && $check_field('get','name_of_skin_disease')">
            <view class="label" v-if="true">
              <span>皮肤病名称</span>
            </view>
              <view class="value">
              <span>{{ o['name_of_skin_disease'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','types_of_skin_diseases')">
            <view class="label" v-if="true">
              <span>皮肤病种类</span>
            </view>
              <view class="value">
              <span>{{ o['types_of_skin_diseases'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','release_date')">
            <view class="label" v-if="true">
              <span>发布日期</span>
            </view>
              <view class="value">
              <span>{{ $toTime(o['release_date'], 'yyyy-MM-dd') }}</span>
            </view>
            </view>
          <view class="item-row " v-if="1 && $check_field('get','treatment_mode')">
            <view class="label" v-if="true">
              <span>治疗方式</span>
            </view>
              <view class="value">
              <span>{{ o['treatment_mode'] }}</span>
            </view>
            </view>
          <view class="item-row diy-image" v-if="1 && $check_field('get','popular_science_pictures')">
            <view class="label" v-if=" false">
              <span>科普图片</span>
            </view>
              <view class="value diy_img">
              <image :src="$fullImgUrl(o['popular_science_pictures'])" width="100%" height="100" />
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','popular_science_video')">
            <view class="label" v-if="true">
              <span>科普视频</span>
            </view>
              <view class="value">
              <span>{{ o['popular_science_video'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','skin_disease_introduction')">
            <view class="label" v-if="true">
              <span>皮肤病介绍</span>
            </view>
              <view class="value">
              <span>{{ o['skin_disease_introduction'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','treatment_plan')">
            <view class="label" v-if="true">
              <span>治疗方案</span>
            </view>
              <view class="value">
              <span>{{ o['treatment_plan'] }}</span>
            </view>
            </view>
          <view class="item-row " v-if="0 && $check_field('get','science_popularization_details')">
            <view class="label" v-if="true">
              <span>科普详情</span>
            </view>
              <view class="value">
              <span>{{ o['science_popularization_details'] }}</span>
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
                  v-if="$check_action('/knowledge_popularization/table', 'del') || $check_action('/knowledge_popularization/view', 'del')">
              删除
            </view>
            <view width="100%" class="me-btn btn-info" @click="$navTo('/pagesC/knowledge_popularization/view?' + field + '=' + o[field])"
                  v-if="$check_action('/knowledge_popularization/table', 'set') || $check_action('/knowledge_popularization/view', 'set') || $check_action('/knowledge_popularization/view', 'get')">
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
      url_get_list: '~/api/knowledge_popularization/get_list?like=0',
      url_del: '~/api/knowledge_popularization/del',

      // 字段ID
      field: 'knowledge_popularization_id',

      // 查询
      query: {
        size: 7,
        page: 1,
              name_of_skin_disease: "",
                    types_of_skin_diseases: "",
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