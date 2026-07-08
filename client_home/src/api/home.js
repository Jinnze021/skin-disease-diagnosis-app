import http from '@/utils/http2';
import { getListParams } from './params/common';

/**
 * 获取首页点赞
 */
export const getPraiseListApi = (params) => {
  return http.request({
    url: `/praise/list_group`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      isLoading: true,
    },
    params,
  });
};

/**
 * 获取首页轮播图列表
 */
export const gethomeSlidesListApi = (params) => {
  params = Object.assign(getListParams(), params);
  return http.request({
    url: `/slides/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      isLoading: true,
    },
    params,
  });
};

/**
 * 获取首页菜单
 */
export const getMenuListApi = (params) => {
  return http.request({
    url: `/auth/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      isLoading: true,
    },
    params,
  });
};
/**
 * 获取首页论坛列表
 */
export const getHomeForumListApi = (params) => {
  params = Object.assign(getListParams(), params);
  return http.request({
    url: `/forum/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      //   isLoading: false,
    },
    params,
  });
};
/**
 * 获取首页新闻资讯列表
 */
export const getHomeArticleListApi = (params) => {
  params = Object.assign(getListParams(), params);
  return http.request({
    url: `/article/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      //   isLoading: false,
    },
    params,
  });
};
/**
 * 获取公告列表
 */
export const getHomeNoticeListApi = (params) => {
  params = Object.assign(getListParams(), params);
  return http.request({
    url: `/notice/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      //   isLoading: false,
    },
    params,
  });
};

	export const getHomeGoodsSearchListApi = (params) => {
	  return;
	};


          /**
   * 获取医院信息
   */
  export const get_hospital_information_api = (params) => {
    let url = "/hospital_information/get_list";
    url = "/hospital_information/get_hits_list";
    return http.request({
      url: url,
      method: 'GET',
      custom: {
        isDeduplication: false,
      },
      params,
    });
  };
    
/**
 * 获取首页分类列表
 */
export const getHomeClassificationListApi = (params) => {
  params = Object.assign(getListParams(), params);
  return http.request({
    url: `/classification_information/get_list`,
    method: 'GET',
    custom: {
      isDeduplication: false,
      //   isLoading: false,
    },
    params,
  });
};



function dateFormat(fmt) {
  var myDate = new Date();
  var o = {
    "M+": myDate.getMonth() + 1, // 月份
    "d+": myDate.getDate(), // 日
    "h+": myDate.getHours(), // 小时
    "m+": myDate.getMinutes(), // 分
    "s+": myDate.getSeconds(), // 秒
    "q+": Math.floor((myDate.getMonth() + 3) / 3), // 季度
    S: myDate.getMilliseconds(), // 毫秒
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
            RegExp.$1,
            (myDate.getFullYear() + "").substr(4 - RegExp.$1.length)
    );
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(
              RegExp.$1,
              RegExp.$1.length == 1
                      ? o[k]
                      : ("00" + o[k]).substr(("" + o[k]).length)
      );
  return fmt;
}
