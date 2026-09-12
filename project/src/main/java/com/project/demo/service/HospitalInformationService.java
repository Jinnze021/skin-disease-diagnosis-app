package com.project.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.demo.entity.HospitalInformation;
import com.project.demo.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 医院信息：(HospitalInformation)表服务接口
 *
 */
@Service
public class HospitalInformationService extends BaseService<HospitalInformation> {

    public Map<String, Object> latest(int limit) {
        QueryWrapper<HospitalInformation> query = new QueryWrapper<HospitalInformation>()
                .orderByDesc("create_time");
        IPage<HospitalInformation> page = baseMapper.selectPage(new Page<>(1, limit), query);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("list", page.getRecords());
        result.put("count", page.getTotal());
        return result;
    }
}
