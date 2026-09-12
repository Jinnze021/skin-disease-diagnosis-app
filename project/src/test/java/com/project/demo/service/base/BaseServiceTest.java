package com.project.demo.service.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.demo.entity.User;
import com.project.demo.entity.SkinDetection;
import com.project.demo.query.EntityFieldRegistry;
import com.project.demo.query.QuerySpec;
import com.project.demo.query.QuerySpecParser;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseServiceTest {

    @Test
    void bindsFilterValuesInsteadOfEmbeddingThemInSql() {
        String attack = "a' OR '1'='1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", attack);
        QuerySpec spec = new QuerySpecParser(new EntityFieldRegistry()).parse(User.class, request);
        BaseService<User> service = new BaseService<User>() { };

        QueryWrapper<User> wrapper = service.buildWhere(spec);

        assertFalse(wrapper.getSqlSegment().contains(attack));
        assertTrue(wrapper.getParamNameValuePairs().containsValue(attack));
    }

    @Test
    void bindsLikeAndRangeValues() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("nickname", "张%'");
        request.setParameter("state_min", "1");
        request.setParameter("like", "true");
        QuerySpec spec = new QuerySpecParser(new EntityFieldRegistry()).parse(User.class, request);
        BaseService<User> service = new BaseService<User>() { };

        QueryWrapper<User> wrapper = service.buildWhere(spec);

        assertFalse(wrapper.getSqlSegment().contains("张%'"));
        assertTrue(wrapper.getParamNameValuePairs().values().stream()
                .map(String::valueOf).anyMatch(value -> value.contains("张%'")));
        assertTrue(wrapper.getParamNameValuePairs().containsValue("1"));
    }

    @Test
    void serverSideOwnerScopeOverridesRequestedUserId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("registered_user", "999");
        QuerySpec requested = new QuerySpecParser(new EntityFieldRegistry())
                .parse(SkinDetection.class, request);
        BaseService<SkinDetection> service = new BaseService<SkinDetection>() { };

        QueryWrapper<SkinDetection> wrapper = service.buildWhere(
                service.scopeToAuthenticatedUser(requested, 7));

        wrapper.getSqlSegment();
        assertTrue(wrapper.getParamNameValuePairs().containsValue("7"),
                wrapper.getParamNameValuePairs().toString());
        assertFalse(wrapper.getParamNameValuePairs().containsValue("999"));
    }
}
