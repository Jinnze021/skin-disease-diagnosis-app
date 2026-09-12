<template>
  <view id="home" class="page_home" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
	<view class="header_bg"></view>
    <tn-nav-bar fixed :isBack="false">
      <view class="nav-wrapper">
        <view class="nav-user" @click="$navTo('/pages/user/info')">
          <image :src="$fullImgUrl(userInfo.avatar) || '/static/img/default.png'"></image>
        </view>
        <view class="tabs">
          <view :class="['tab-item', tabIndex === 0 ? 'active' : '']" @click="onClickTab(0)">
            <view class="name">推荐</view>
            <view class="active-flag" v-if="tabIndex === 0"> </view>
          </view>
                  <view :class="['tab-item', tabIndex === 4 ? 'active' : '']" @click="onClickTab(4)" v-if="$check_action('/hospital_information/list', 'get')">
            <view class="name">医院信息</view>
            <view class="active-flag" v-if="tabIndex === 4"> </view>
          </view>
            </view>
      </view>
    </tn-nav-bar>

    <view class="tab-pane" v-show="tabIndex === 0">
      <view class="search-wrapper" @click.stop="$navTo('/pages/search/index')">
        <Search disabled placeholder="搜索内容" @cancel="cancel" />
      </view>

      <view class="scroll-x">
        <view class="left">
          <scroll-view class="scroll-view" :style="[!isExpand ? expandStyle : '']" scroll-x="true">
		    <view class="item">
		      推荐
		    </view>
            <view
              class="item"
              @click="clickMore(item.path)"
              v-for="(item, index) in list_menu"
              :key="index"
			   v-if="$check_action(item.path,'get') || item.path == '/webview/urlview' || item.path == '/chat/index'"
            >
              {{ item.mod_name }}
            </view>
          </scroll-view>
        </view>
        <view class="right">
          <view class="iconfont icon-liebiao" @click="isExpand = !isExpand"></view
        ></view>
      </view>

      <!-- 轮播图模块(开始) -->
      <view class="swiper">
        <Slide :list="list_slide" :show_title="true" />
      </view>
      <!-- 轮播图模块(结束) -->

      <!-- 菜单模块(开始) -->
      <view class="menu" v-if="list_menu.length > 0">
        <list_menu :list="list_menu"></list_menu>
      </view>
      <!-- 菜单模块(结束) -->

      <!-- 新闻资讯模块(开始) -->
      <Card
        v-if="$check_action('/article/list', 'get')"
        title="新闻资讯"
        url="/pages/article/index"
	    toTab
        :list="list_article"
        class="article_list"
      >
        <view class="scroll-category">
          <scroll-view class="scroll-view" scroll-x="true">
            <view
              :class="['category-item', activeArticle == item.value ? 'active' : '']"
              @click="onClickCategory(item.value)"
              v-for="(item, index) in types_article"
              :key="index"
            >
              {{ item.value }}</view
            >
          </scroll-view>
        </view>
        <list_article :list="list_article"></list_article>
      </Card>
      <view class="split-line"></view>
      <!-- 新闻资讯模块(结束) -->
	  <view class="home_recommend">
        		<Card
		  v-if="$check_action('/hospital_information/list', 'get')"
		  class="list_diy"
		  title="医院信息"
		  url="/pagesC/hospital_information/list"
		  :list="list_hospital_information"
		>
		  <list_hospital_information :list="list_hospital_information"></list_hospital_information>
		</Card>
    	  </view>
      <Card
        v-if="$check_action('/notice/list', 'get')"
        class="notice_list list_diy"
        title="公告信息"
        url="/pages/notice/list"
        :list="list_notice"
      >
        <view class="gg-list-wrap">
          <view
            class="gg-list-item"
            v-for="(o, i) in list_notice"
            :key="i"
            @click="$navTo('/pages/notice/details?notice_id=' + o['notice_id'])"
          >
            <view class="content">
              <text class="num">{{ i + 1 }}.</text>
              <text class="title">{{ o.title }}</text>
            </view>
			<view class="time"> {{ $toTime(o.create_time, "yyyy-MM-dd hh:mm:ss") }}</view>
			<rich-text class="desc" :nodes="$setRichTextImage(o.content)"></rich-text>
          </view>
        </view>
      </Card>

      <view class="split-line"></view>
      <!-- 推荐通知公告模块(结束) -->


      <!-- 版权模块(开始) -->
      <view class="copyright">
        <text>@版权归属 XX 所有</text>
      </view>
      <!-- 版权模块(结束) -->
    </view>

            <view class="tab-pane" v-show="tabIndex === 4">
      <view class="split-line"></view>
      <view
        v-if="$check_action('/hospital_information/list', 'get')"
        class="list_diy"
        url="/pagesC/hospital_information/list"
      >
        <list_hospital_information :list="list_hospital_information"></list_hospital_information>
      </view>
      <view class="see-more">
        <view @click="$navTo('/pagesC/hospital_information/list')">
          查看更多<text class="iconfont icon-right"></text>
        </view>
      </view>

      <view class="split-line"></view>
    </view>
    


    <tn-tabbar
      :value="tabbarIndex"
      :list="tabbarList"
      @change="switchTabbar"
      :animation="true"
      :safeAreaInsetBottom="true"
    ></tn-tabbar>
  </view>
</template>

<script>
import Search from '@/components/businessCp/search.vue';
        import list_hospital_information from "@/components/diy/list_hospital_information.vue";
    import Card from '@/components/common/card.vue';
import list_menu from '@/components/diy/list_menu.vue';
import list_article from '@/components/diy/list_article.vue';
import Slide from '@/components/common/slide.vue';
import Notice from '@/components/common/notice.vue';

import mixin from '@/libs/mixins/page.js';
import tabbar from '@/libs/mixins/tabbar.js';
import * as HomeApi from '@/api/home';

export default {
  mixins: [mixin, tabbar],
  components: {
    Search,
    Card,
            list_hospital_information,
        list_menu,
    list_article,
    Slide,
    Notice,
  },
  data() {
    return {
      tabIndex: 0,
      isExpand: false,
      expandStyle: {
        whiteSpace: 'nowrap',
      },
      tabbarIndex: 0,
              list_hospital_information: [],
          list_slide: [],
      list_article: [],
      list_menu: [],
      list_notice: [],
      activeArticle: '全部',
      types_article: [],
    };
  },
  created() {
  },
  methods: {
    onClickCategory(val) {
      this.activeArticle = val;
      this.get_article();
    },
    /**
     *  获取文章
     */
    get_article() {
      let params = {
        page: 1,
        size: 6,
      };
      if (this.activeArticle && this.activeArticle != '全部') {
        params['type'] = this.activeArticle;
      }
      HomeApi.getHomeArticleListApi(params).then((res) => {
        if (res.result && res.result.list) {
          this.list_article = res.result.list;
        }
      });
    },
    /**
     * 添加文章过滤
     */
    get_article_type() {
      this.$get(
        '~/api/article_type/get_list',
        {
          page: 1,
        },
        (res) => {
          if (res.result && res.result.list) {
            let list = res.result.list.map((obj) => {
              return { value: obj.name, text: obj.name };
            });
            let arr = [
              {
                value: '全部',
                text: '全部',
              },
            ];
            this.types_article = arr.concat(list);
          }
        }
      );
    },
    getFullPath(path) {
      if (path) {
        const fullPath = this.pathList[path];
        if (fullPath) {
          return fullPath + path;
        } else {
          return '/pagesC' + path;
        }
      }
      return '';
    },
    onClickTab(val) {
      this.tabIndex = val;
    },
                    /**
     *  获取医院信息
     */
    get_hospital_information() {
      const params = {
            user_id: this.userInfo.user_id,
		size: 12,
          };
          if(this.userInfo.user_group=="管理员"){
        delete param.user_id;
      }
          HomeApi.get_hospital_information_api(params).then((res) => {
        if (res.result && res.result.list) {
          this.list_hospital_information = res.result.list;
        }
      });
    },
      
    /**
     *  获取轮播图
     */
    get_slides() {
      HomeApi.gethomeSlidesListApi().then((res) => {
        if (res.result && res.result.list) {
          this.list_slide = res.result.list;
        }
      });
    },

    /**
     *  获取导航栏
     */
    get_menu() {
      const user_group = this.userGroup;
      HomeApi.getMenuListApi({
        size: '0',
        get: 1,
        user_group,
        position: 'top',
      }).then((res) => {
        if (res.result && res.result.list) {
          this.list_menu = res.result.list;
        }
		this.list_menu.push({mod_name:"新闻资讯",path:"/article/index"})
		this.list_menu.push({mod_name:"病友交流",path:"/forum/index"})
      });
    },

    /**
     *  获取公告列表
     */
    get_notice() {
      HomeApi.getHomeNoticeListApi({ page: 1, size: 3 }).then((res) => {
        if (res.result && res.result.list) {
          // console.log('公告', res.result.list);
          this.list_notice = res.result.list;
          this.list_notice.map((o) => {
            o['praise_len'];
          });
          this.get_praise(this.list_notice, 'notice', 'notice_id');
        }
      });
    },
    /**
     *  获取点赞数
     *  @param {Object} list
     */
    get_praise(list, table, idName) {
      if (list) {
        for (let i = 0; i < list.length; i++) {
          list[i].praise_len = 0;
        }

        HomeApi.getPraiseListApi({
          source_table: table,
          groupby: 'source_id',
        }).then((res) => {
          if (res.result && res.result.list) {
            res.result.list.map((o) => {
              for (let i = 0; i < list.length; i++) {
                let oj = list[i];
                if (oj[idName] === o['source_id']) {
                  oj['praise_len'] = o['count'];
                  break;
                }
              }
            });
          } else if (res.error) {
            // console.error(res.error);
          }
        });
      }
    },
	clickMore(url){
		if(url == "/article/index" || url == "/forum/index"){
			this.$navToTab(this.getFullPath(url))
		}else{
			this.$navTo(this.getFullPath(url))
		}
	}
  },
  onShow() {
    this.get_slides();
    this.get_menu();
    this.get_notice();
            this.get_hospital_information();
        this.get_article_type();
    this.get_article();
  },
};
</script>
<style lang="scss" scoped>
@import 'styles/pages/index.scss';
</style>
