<template>
  <view class="notice-bar" v-if="dataList.length > 0">
    <tn-notice-bar
      :list="dataList"
      mode="vertical"
      align="center"
      fontColor="#FFFFFF"
    ></tn-notice-bar>
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
    vm: {
      type: Object,
      default: function () {
        return {
          img: 'img',
          title: 'title',
        };
      },
    },
  },
  data() {
    return {
      dataList: [],
    };
  },
  watch: {
    list(newVal) {
      this.dataList = newVal.map((item) => item[this.vm.title]);
    },
  },
  created() {
    this.dataList = this.list.map((item) => item[this.vm.title]);
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import 'styles/components/common.scss';
</style>
