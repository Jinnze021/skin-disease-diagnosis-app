<template>
  <view class="cp-list-wrap">
    <!-- 视图 -->
    <view class="cp-list-item" v-for="(o, i) in list" :key="i">
        <view class="item-row " v-if="1 && $check_index_field('get', 'hospital_name', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院名称</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_name'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="1 && $check_index_field('get', 'hospital_address', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院地址</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_address'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="1 && $check_index_field('get', 'type_of_hospital', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院类型</span>
        </view>
            <view class="value text">
          <span>{{ o['type_of_hospital'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'distance_from_hospital', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院距离</span>
        </view>
            <view class="value text">
          <span>{{ o['distance_from_hospital'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'hospital_phone_number', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院电话</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_phone_number'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'hospital_score', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院评分</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_score'] }}</span>
        </view>
          </view>
          <view class="item-row diy-image" v-if="1 && $check_index_field('get', 'hospital_pictures', '/hospital_information/list')">
        <view class="label" v-if=" false">
          <span>医院图片</span>
        </view>
            <view class="value">
          <image :src="$fullImgUrl(o['hospital_pictures']) || '/static/img/default.png'" mode="scaleToFill" />
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'areas_of_expertise', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>擅长领域</span>
        </view>
            <view class="value text">
          <span>{{ o['areas_of_expertise'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'hospital_introduction', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院介绍</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_introduction'] }}</span>
        </view>
          </view>
          <view class="item-row " v-if="0 && $check_index_field('get', 'hospital_details', '/hospital_information/list')">
        <view class="label" v-if="true">
          <span>医院详情</span>
        </view>
            <view class="value text">
          <span>{{ o['hospital_details'] }}</span>
        </view>
          </view>
        <view class="content">
        <view class="item-row">
          <view class="value">
            <text class="icon iconfont icon-chakan"></text>
            <text class="text">{{ o['praise_len'] }}</text>
          </view>
        </view>
        <view class="item-row">
          <view class="value">
            <text class="icon iconfont icon-dianzan"></text><text>{{ o['hits'] }}</text>
          </view>
        </view>
      </view>

      <view class="item-row">
        <view class="create_time_block value number">
          <span>{{ $toTime(o.create_time, 'yyyy-MM-dd hh:mm:ss') }}</span>
        </view>
      </view>

      <view class="me-btn btn-see" @click="
          $navTo('/pagesC/hospital_information/details?hospital_information_id=' +
              o['hospital_information_id']
          )
        "
        >查看详情</view
      >
    </view>
    <!-- /视图 -->
  </view>
</template>

<script>
export default {
  props: {
    list: {
      type: Array,
      default: function () {
        return [];
      },
    },
  },
  data() {
    return {
                        };
  },
  methods: {
                      },
  created() {
                      },
};
</script>

<style lang="scss" scoped>
@import 'styles/components/index.scss';
</style>