<template>
  <view :style="{ paddingTop: vuex_custom_bar_height + 'px' }" class="page_diy_list page_knowledge_popularization_list container">
	<view class="content">
	  <tn-nav-bar>知识科普列表</tn-nav-bar>
	  <view class="page-list" id="page_diy_list">
	        <!-- 筛选模块(开始) -->
	        <view class="search-wrap">
	  	    	      	          <Search v-model="query.name_of_skin_disease" placeholder="搜索皮肤病名称" @search="search_" @cancel="search_cancel('name_of_skin_disease')" @input="(val) => inputValue(val, 'name_of_skin_disease')" />
	      	    	  	    	      	          <Search v-model="query.types_of_skin_diseases" placeholder="搜索皮肤病种类" @search="search_" @cancel="search_cancel('types_of_skin_diseases')" @input="(val) => inputValue(val, 'types_of_skin_diseases')" />
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
	              @click=" $navTo('/pagesC/knowledge_popularization/details?knowledge_popularization_id=' + o['knowledge_popularization_id'])">
	  	              <view class="item-row " v-if="1 && $check_field('get', 'name_of_skin_disease')">
	                <view class="label" v-if="true">
	                  <span>皮肤病名称</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['name_of_skin_disease'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="1 && $check_field('get', 'types_of_skin_diseases')">
	                <view class="label" v-if="true">
	                  <span>皮肤病种类</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['types_of_skin_diseases'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="1 && $check_field('get', 'release_date')">
	                <view class="label" v-if="true">
	                  <span>发布日期</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ $toTime(o['release_date'], 'yyyy-MM-dd') }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="1 && $check_field('get', 'treatment_mode')">
	                <view class="label" v-if="true">
	                  <span>治疗方式</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['treatment_mode'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row diy-image" v-if="1 && $check_field('get', 'popular_science_pictures')">
	                <view class="label" v-if=" false">
	                  <span>科普图片</span>
	                </view>
	    	                <view class="value diy_img">
	                  <image :src="$fullImgUrl(o['popular_science_pictures'])" width="100%" height="100" />
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'popular_science_video')">
	                <view class="label" v-if="true">
	                  <span>科普视频</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['popular_science_video'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'skin_disease_introduction')">
	                <view class="label" v-if="true">
	                  <span>皮肤病介绍</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['skin_disease_introduction'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'treatment_plan')">
	                <view class="label" v-if="true">
	                  <span>治疗方案</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['treatment_plan'] }}</span>
	                </view>
	    	              </view>
	  	              <view class="item-row " v-if="0 && $check_field('get', 'science_popularization_details')">
	                <view class="label" v-if="true">
	                  <span>科普详情</span>
	                </view>
	    	                <view class="value">
	                  <span>{{ o['science_popularization_details'] }}</span>
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
      url_get_list: '~/api/knowledge_popularization/get_list?like=0',
                                          query: {
                name_of_skin_disease: "", // 皮肤病名称
                        types_of_skin_diseases: "", // 皮肤病种类
                              knowledge_popularization_id: 0, // ID
        page: 1,
        size: 10,
      },
      list: [],
      count: 50,
      menuList: [
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
		this.$get('~/api/praise/list_group?groupby=source_id&source_table=knowledge_popularization',{},(res) => {
		    if (res.result && res.result.list) {
		      res.result.list.map((o) => {
		        for (var i = 0; i < list.length; i++) {
		          var oj = list[i];
		          if (oj['knowledge_popularization_id'] === o['source_id']) {
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