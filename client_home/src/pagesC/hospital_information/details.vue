<template>
  <view id="page_diy_details" class="page_diy_details page_hospital_information_details" :style="{ paddingTop: vuex_custom_bar_height + 'px', paddingBottom: vuex_safe_area_bottom + 'px',}">
    <tn-nav-bar>医院信息</tn-nav-bar>
    <view class="page_diy page_hospital_information" id="hospital_information_details">
                    <view class="warp cover_wrapper" v-if="$check_field('get', 'hospital_pictures')">
        <image class="diy_img diy-image" :src="$fullImgUrl(obj['hospital_pictures'])"/>
      </view>
        
      <view class="warp info-wrapper">
        <view class="container-fluid">
          <view class="row">
              <view v-if="$check_field('get', 'hospital_name')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院名称</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["hospital_name"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'hospital_address')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院地址</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["hospital_address"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'type_of_hospital')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院类型</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["type_of_hospital"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'distance_from_hospital')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院距离</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["distance_from_hospital"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'hospital_phone_number')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院电话</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["hospital_phone_number"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'hospital_score')" class="info-item  field_text ">
                <view class="diy_title">
                  <span>医院评分</span>
                </view>
                    <view class="diy_field diy_text">
                  <span>
                      {{ obj["hospital_score"] }}
                  </span>
                </view>
                </view>
                  <view v-if="$check_field('get', 'areas_of_expertise')" class="info-item field_text ">
                <view class="diy_title">
                  <span>擅长领域</span>
                </view>
                    <view class="diy_field diy_desc">
                  <span>
                      {{ obj["areas_of_expertise"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'hospital_introduction')" class="info-item field_text ">
                <view class="diy_title">
                  <span>医院介绍</span>
                </view>
                    <view class="diy_field diy_desc">
                  <span>
                      {{ obj["hospital_introduction"] }}
                  </span>
                </view>
                </view>
                <view v-if="$check_field('get', 'hospital_details')" class="info-item field_text ">
                <view class="diy_title">
                  <span>医院详情</span>
                </view>
                    <view class="diy_field diy_html">
                  <rich-text class="rich_text" :nodes="obj['hospital_details']"></rich-text>
                </view>
                </view>
            </view>
        </view>
        <view class="row count">
              <view class="praise view">
              <span>点赞</span>
              <span>{{ obj["praise_len"] }}</span>
            </view>
  
  
              <view class="hits view">
              <span>点击数</span>
              <span>{{ obj["hits"] }}</span>
            </view>
          </view>
      </view>

	<view class="kb-row">


	</view>


      <!-- 评论区列表 -->
      <view class="comment-title">评论区</view>
      <list_comment :list="list_comment" @refresh="get_comment"></list_comment>

      <view :style="'height: 100rpx'"></view>
      <view class="footer" :style="{ paddingBottom: vuex_safe_area_bottom + 'px' }">
        <view class="footer-wrap">
              <view class="go-comment" @click="goComment('/pagesB/comment/edit?source_table=hospital_information&source_field=hospital_information_id&source_id=' + obj['hospital_information_id'])">
            评论点什么...
          </view>
    
              <view class="more">
                    <view class="more-item praise" @click="change_praise()">
              <text class="iconfont icon-praise iconfont--active" v-if="state_praise"></text>
              <text class="iconfont icon-praise" v-else></text>
              <text>点赞</text>
            </view>
        
                    <view class="more-item collect" @click="change_collect()">
              <text :class="{iconfont: true, 'icon-collect': true, 'iconfont--active': state_collect,}"></text>
              <text>收藏</text>
            </view>
        
                  </view>
            </view>
      </view>


    </view>
  </view>
</template>

<script>
  import list_comment from '@/components/diy/list_comment.vue';
  import mixin from '@/libs/mixins/page.js';

  export default {
    mixins: [mixin],
    components: {
      list_comment
    },
    data() {
      return {
        url_get_obj: '~/api/hospital_information/get_obj?',
        field: 'hospital_information_id',
        query: {
                hospital_information_id: 0,
        },
        // 商品详情初始化
        obj: {
          hospital_information_id: 0,
              hospital_name: "",
                  hospital_address: "",
                  type_of_hospital: "",
                  distance_from_hospital: "",
                  hospital_phone_number: "",
                  hospital_score: "",
                  hospital_pictures: "",
                  areas_of_expertise: "",
                  hospital_introduction: "",
                  hospital_details: "",
              hits: 0,
          praise_len: 0,
          collect_len: 0,
        },
        // 点赞状态
        state_praise: false,
        // 点赞
        praise: 0,
        // 收藏
        collect: 0,
        // 收藏状态
        state_collect: false,
        // 评论列表初始化
        list_comment: [],
                          };
    },
    methods: {
      goComment(path) {
        if (this.$check_action('/comment/list', 'add')) {
          this.$navTo(path);
        } else {
          this.$toast('暂无权限');
        }
      },
      openUrl(url) {
        uni.navigateTo({
          url: `/pages/webview/webview?url=${url}`,
        });
      },
      /**
       * 获取点赞
       * @param {Object} obj
       */
      get_praise(obj) {
        var user_id = this.user.user_id;

        this.obj.praise_len = 0;

        this.$get(
            '~/api/praise/count',
            {
              source_table: 'hospital_information',
              source_field: 'hospital_information_id',
              source_id: obj['hospital_information_id'],
            },
            (res) => {
              if (res.result || res.result === 0) {
                this.praise = res.result;
                this.obj.praise_len = res.result;
                console.log('点赞数：', res.result);
              } else if (res.error) {
                this.$toast(res.error.message);
                console.error(res.error);
              }
            }
        );

        this.$get(
            '~/api/praise/count',
            {
              source_table: 'hospital_information',
              source_field: 'hospital_information_id',
              source_id: obj['hospital_information_id'],
              user_id,
            },
            (res) => {
              if (res.result || res.result === 0) {
                this.state_praise = res.result ? true : false;
                console.log('点赞状态：', this.state_praise);
              } else if (res.error) {
                this.$toast(res.error.message);
                console.error(res.error);
              }
            }
        );
      },

      /**
       * 改变点赞数
       */
      change_praise() {
        var user_id = this.userInfo.user_id;

        var query = {
          source_table: 'hospital_information',
          source_field: 'hospital_information_id',
          source_id: this.obj['hospital_information_id'],
          user_id,
        };

        var _this = this;
        _this.obj.praise_len = parseInt(_this.obj.praise_len);
        // 点赞状态
        if (this.state_praise) {
          this.state_praise = false;
          this.$get('~/api/praise/del', query, (res) => {
            if (res.result) {
              var praise_len = _this.obj.praise_len - 1;
              this.$post('~/api/hospital_information/set?hospital_information_id=' + _this.obj['hospital_information_id'],
                  {praise_len},(res) => {
                    if (res.result) {
                      _this.obj.praise_len = praise_len;
                      console.log('添加点赞数状态：', res.result);
                    } else if (res.error) {
                      console.error(res.error);
                    }
                  }
              );
              this.$toast('取消点赞');
            } else if (res.error) {
              this.$toast(res.error.message);
              console.error(res.error);
            }
          });
        } else {
          this.state_praise = true;
          this.$post('~/api/praise/add?', query, (res) => {
            if (res.result) {
              var praise_len = _this.obj.praise_len + 1;
              this.$post('~/api/hospital_information/set?hospital_information_id=' + _this.obj['hospital_information_id'],
                  {praise_len},(res) => {
                    if (res.result) {
                      _this.obj.praise_len = praise_len;
                      console.log('添加点赞数状态：', res.result);
                    } else if (res.error) {
                      console.error(res.error);
                    }
                  }
              );
              this.$toast('点赞成功');
            } else if (res.error) {
              this.$toast(res.error.message);
              console.error(res.error);
            }
          });
        }
      },
      /**
       * 初始化收藏状态
       * @param {Object} obj
       */
      get_collect(obj) {
        var user_id = this.user.user_id;

        var query = {
          source_table: 'hospital_information',
          source_field: 'hospital_information_id',
          source_id: obj['hospital_information_id'],
          user_id,
        };

        this.$get('~/api/collect/count', query, (res) => {
          if (res.result || res.result === 0) {
            var bl = res.result ? true : false;
            this.state_collect = bl;
            console.log('收藏状态：' + bl);
          } else if (res.error) {
            console.error(res.error);
          }
        });
      },

      /**
       * 改变收藏状态
       */
      change_collect() {
        if (!this.$check_action('/collect/list', 'add')) {
          this.$toast('暂无权限');
          return;
        }
        var user_id = this.user.user_id;

        var query = {
          source_table: 'hospital_information',
          source_field: 'hospital_information_id',
          source_id: this.obj['hospital_information_id'],
          user_id,
        };

        // 收藏状态
		var _this = this;
        if (this.state_collect) {
          this.state_collect = false;
          this.$get('~/api/collect/del', query, (res) => {
            if (res.result) {
			  var collect_len = _this.obj.collect_len - 1;
			  this.$post('~/api/hospital_information/set?hospital_information_id=' + _this.obj['hospital_information_id'],
			      {collect_len},(res) => {
			        if (res.result) {
			          _this.obj.collect_len = collect_len;
			          console.log('添加收藏数状态：', res.result);
			        } else if (res.error) {
			          console.error(res.error);
			        }
			      }
			  );
              this.$toast('取消收藏');
            } else if (res.error) {
              this.$toast(res.error.message);
              console.error(res.error);
            }
          });
        } else {
          this.state_collect = true;
              query.title = this.obj.hospital_name
                                                                  query.img = this.obj.hospital_pictures
                                      this.$post('~/api/collect/add?', query, (res) => {
            if (res.result) {
			  var collect_len = _this.obj.collect_len + 1;
			  this.$post('~/api/hospital_information/set?hospital_information_id=' + _this.obj['hospital_information_id'],
			      {collect_len},(res) => {
			        if (res.result) {
			          _this.obj.collect_len = collect_len;
			          console.log('添加收藏数状态：', res.result);
			        } else if (res.error) {
			          console.error(res.error);
			        }
			      }
			  );
              this.$toast('收藏成功');
            } else if (res.error) {
              this.$toast(res.error.message);
              console.error(res.error);
            }
          });
        }
      },
      /**
       * 获取评论
       * @param {Object} obj
       */
      get_comment(obj) {
		if(obj == undefined){
			obj = this.obj
		}
        var query = {
          source_table: 'hospital_information',
          source_field: 'hospital_information_id',
          source_id: obj['hospital_information_id'],
          orderby: 'create_time desc',
          reply_to_id: '0',
        };
        this.$get('/comment/get_list', query, (json) => {
          if (json.result) {
            var list_comment = json.result.list;
            list_comment.map((o) => {
              o.list_reply = [];
            });
            this.add_reply(obj, list_comment).then((list) => {
              this.list_comment = list;
            });
          }
        });
      },
      /**
       * @param { Array } list 评论列表
       * 添加回复到评论列表
       */
      add_reply(obj, list) {
        return new Promise((resolve) => {
          for (let idx = 0; idx < list.length; idx++) {
            const obj = list[idx];
            this.$get('/comment/get_list',
                {
                  source_table: 'hospital_information',
                  source_field: 'hospital_information_id',
                  source_id: obj['hospital_information_id'],
                  orderby: 'create_time desc',
                  reply_to_id: obj.comment_id,
                },
                (res) => {
                  if (res.result) {
                    obj.list_reply = res.result.list;
                  }
                }
            );
          }
          resolve(list);
        }).catch((e) => {
        });
      },
      /**
       * 添加访问量
       */
      add_hits(obj) {
        obj['hits'] = obj['hits'] + 1;
        var hits = obj['hits'];
        this.$post(
            '~/api/hospital_information/set?hospital_information_id=' + obj['hospital_information_id'],
            {
              hits,
            },
            (res) => {
              if (res.result) {
                console.log('添加访问量状态：', res.result);
                var body = {
                  source_table: 'hospital_information',
                  source_field: 'hospital_information_id',
                  source_id: this.obj.hospital_information_id,
                  user_id: this.userInfo.user_id,
                };
                this.$post('~/api/hits/add?', body, (res) => {
                  console.log(res);
                });
              } else if (res.error) {
                console.error(res.error);
              }
            }
        );
      },
      /**
       * 获取对象之后
       * @param {Object} json 结果对象
       */
      get_obj_after(json) {
        // 判断是否获取到数据
        if (this.obj) {
          var obj = this.obj;
          // 获取点赞数
          this.get_praise(obj);
          // 初始化收藏状态
          this.get_collect(obj);
          // 获取评论
          this.get_comment(obj);
          // 增加点击数
          this.add_hits(obj);
        }
      },
                                            },
    created() {
                                            },
    onShow() {
        
    },
  };
</script>

<style lang="scss" scoped>
  @import 'styles/pagesC/index.scss';
</style>