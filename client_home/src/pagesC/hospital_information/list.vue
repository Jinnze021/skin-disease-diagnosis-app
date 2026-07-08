<template>
  <view :style="{ paddingTop: vuex_custom_bar_height + 'px' }" class="page_diy_list page_hospital_information_list container">
	<view class="content">
	  <tn-nav-bar>医院信息列表</tn-nav-bar>
	  <view class="page-list" id="page_diy_list">
	        <!-- 筛选模块(开始) -->
	        <view class="search-wrap">
	  	    	      	          <Search v-model="query.hospital_name" placeholder="搜索医院名称" @search="search_" @cancel="search_cancel('hospital_name')" @input="(val) => inputValue(val, 'hospital_name')" />
	      	    	  	    	      	          <Search v-model="query.hospital_address" placeholder="搜索医院地址" @search="search_" @cancel="search_cancel('hospital_address')" @input="(val) => inputValue(val, 'hospital_address')" />
	      	    	  	    	  	    	  	    	  	    	  	    	  	    	  	    	  	    	  	  	    	      	    	  	    	      	    	  	    	  	    	  	    	  	    	  	    	  	    	  	    	  	    	  	  	          <MeDropdown
	            :menuList.sync="menuList"
	            themeColor="#0079fe"
	            :duration="300"
	            :isCeiling="true"
	            @onConfirm="onConfirmDropdown"
	            @onChange="onChangeDropdown"
	          ></MeDropdown>
	        </view>
	  
	        <!-- 筛选模块(结束) -->
	  <!-- 列表 -->
	        <view class="customized-list">
	  	          <view v-for="(o, i) in list" :key="i" class="customized-item">
	  	  	            <view
	              class="customized-item-body"
	              @click=" $navTo('/pagesC/hospital_information/details?hospital_information_id=' + o['hospital_information_id'])">
	  	              <view class="item-row " v-if="1 && $check_field('get', 'hospital_name')">
	                <view class="label" v-if="true">
	                  <span>医院名称</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_name'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="1 && $check_field('get', 'hospital_address')">
	                <view class="label" v-if="true">
	                  <span>医院地址</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_address'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="1 && $check_field('get', 'type_of_hospital')">
	                <view class="label" v-if="true">
	                  <span>医院类型</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['type_of_hospital'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'distance_from_hospital')">
	                <view class="label" v-if="true">
	                  <span>医院距离</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['distance_from_hospital'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'hospital_phone_number')">
	                <view class="label" v-if="true">
	                  <span>医院电话</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_phone_number'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'hospital_score')">
	                <view class="label" v-if="true">
	                  <span>医院评分</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_score'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row diy-image" v-if="1 && $check_field('get', 'hospital_pictures')">
	                <view class="label" v-if=" false">
	                  <span>医院图片</span>
	                </view>
	    	                <view class="value diy_img">
	                  <image :src="$fullImgUrl(o['hospital_pictures'])" width="100%" height="100" />
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'areas_of_expertise')">
	                <view class="label" v-if="true">
	                  <span>擅长领域</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['areas_of_expertise'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'hospital_introduction')">
	                <view class="label" v-if="true">
	                  <span>医院介绍</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_introduction'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'hospital_details')">
	                <view class="label" v-if="true">
	                  <span>医院详情</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['hospital_details'] }}</span>
	                </view>
	    	              </view>
	  	  	            </view>
	  
	  	            <view class="customized-item-footer">
	    	              <view class="praise">
	                <text class="icon iconfont icon-dianzan"></text>
	                {{ o['praise_len'] || 0 }}
	              </view>
	    	    	              <view class="collect">
	                <text class="icon iconfont icon-shoucang"></text>
	                {{ o['collect_len'] || 0 }}
	              </view>
	    	    	              <view class="comment">
	                <text class="icon iconfont icon-pinglun"></text>
	                {{ o['comment_len'] || 0 }}
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
	</view>
</template>

<script>
import Search from '@/components/businessCp/search.vue';
import mixin from '@/libs/mixins/page.js';
import MeDropdown from '@/components/me-dropdown/index.vue';

export default {
  mixins: [mixin],
  components: {
    Search,
    MeDropdown,
  },
  data() {
    return {
      url_get_list: '~/api/hospital_information/get_list?like=0',
                                              query: {
                hospital_name: "", // 医院名称
                        hospital_address: "", // 医院地址
                                hospital_information_id: 0, // ID
        page: 1,
        size: 10,
      },
      list: [],
      count: 50,
      menuList: [
                {
          title: '医院距离',
          type: 'sort',
          command: '`distance_from_hospital`',
          value: 0,
        },
              {
          title: '医院评分',
          type: 'sort',
          command: '`hospital_score`',
          value: 0,
        },
                  {
            title: '点赞数',
            type: 'sort',
            command: '`praise_len`',
            value: 0,
        },
        {
            title: '点击数',
            type: 'sort',
            command: '`hits`',
            value: 0,
        },
        {
            title: '收藏数',
            type: 'sort',
            command: '`collect_len`',
            value: 0,
        },
        {
            title: '发布时间',
            type: 'sort',
            command: '`create_time`',
            value: 0,
        }
      ],
    };
  },
  methods: {
    onConfirmDropdown(val) {
      if (val.value) {
        this.query.orderby = val.command + ' ' + val.value;
      } else {
        this.query.orderby = '';
      }
      this.search_();
    },
    onChangeDropdown(v) {},
    /**
     * 获取列表后
     * @param {Object} json
     * @param {Object} func
     */
    get_list_after(json, func) {
      let list = json.result.list;
      this.get_praise(list);
      if (func) {
        func(json);
      }
    },
    /**
     * 获取点赞数
     * @param {Object} list
     */
    get_praise(list) {
		for (let i = 0; i < list.length; i++) {
			list[i].praise_len = 0;
		}
		this.$get('~/api/praise/list_group?groupby=source_id&source_table=hospital_information',{},(res) => {
		    if (res.result && res.result.list) {
		      res.result.list.map((o) => {
		        for (var i = 0; i < list.length; i++) {
		          var oj = list[i];
		          if (oj['hospital_information_id'] === o['source_id']) {
		            oj['praise_len'] = o['count'];
		            // break;
		          }
		        }
		      });
		    } else if (res.error) {
		      console.error(res.error);
		    }
		  }
		);
    },
          
              
      
      
      
      
      
      
      
      
        search_() {
      this.query.page = 1;
      this.get_list();
    },
    search_cancel(key) {
      this.query[key] = '';
      this.search_();
    },
	inputValue(val, key) {
	  this.query[key] = val;
	  this.search_();
	},
  },
  created() {
                                          },
};
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>