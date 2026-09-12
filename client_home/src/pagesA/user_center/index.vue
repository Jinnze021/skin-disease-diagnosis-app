<template>
  <view id="page_statistics" class="page_user_center" :style="{ paddingTop: vuex_custom_bar_height + 'px' }">
    <tn-nav-bar>统计图</tn-nav-bar>
    <view class="warp">
      <view class="container">
        <view class="row justify-content-between">
          <view class="col-12 col-md-3">
            <view class="card_menu">
              <!-- 左侧边栏 -->
            </view>
          </view>
          <view class="col-12 col-md-9">
            <view class="card_addres">
              <view class="warp">
                <view class="container-fluid">
                  <view class="d-row-line">
                    <view class="title">数据统计中心</view>
                  </view>
                  <view class="d-row" style="height: 300px">
                            </view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
  import mixin from '@/libs/mixins/page.js';

  export default {
    mixins: [mixin],
    data() {
      return {
                          };
    },
    mounted() {
    },
    methods: {
      async get_nickname(list, flag) {
        if (flag) {
          for (let i = 0; i < list.length; i++) {
            await this.$get('~/api/user/get_obj?user_id=' + list[i], null, (json) => {
              if (json.result) {
                list[i] = json.result.obj.nickname;
              }
            });
          }
        } else {
          for (let i = 0; i < list.length; i++) {
            await this.$get('~/api/user/get_obj?user_id=' + list[i].name, null, (json) => {
              if (json.result) {
                list[i].name = json.result.obj.nickname;
              }
            });
          }
        }
      },
                        },
    created() {
              },
    components: {
    },
  };
</script>
<style lang="scss" scoped>
  @import 'styles/pagesA/index.scss';
</style>
