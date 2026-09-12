package com.project.demo.controller.base;

import com.project.demo.query.QuerySpec;
import com.project.demo.security.RequestUser;
import com.project.demo.service.base.BaseService;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseController<E, S extends BaseService<E>> {

    @Setter
    protected S service;

    @PostMapping("/add")
    @Transactional
    public Map<String, Object> add(HttpServletRequest request) throws IOException {
        Map<String, Object> body = service.readBody(request.getReader());
        service.scopeBodyToAuthenticatedUser(body, scopedUserId(request));
        service.insert(body);
        return success(1);
    }

    @PostMapping("/set")
    @Transactional
    public Map<String, Object> set(HttpServletRequest request) throws IOException {
        Map<String, Object> body = service.readBody(request.getReader());
        service.scopeBodyToAuthenticatedUser(body, scopedUserId(request));
        service.update(scopedMutationSpec(request), body);
        return success(1);
    }

    @RequestMapping("/del")
    @Transactional
    public Map<String, Object> del(HttpServletRequest request) {
        service.delete(scopedMutationSpec(request));
        return success(1);
    }

    @RequestMapping("/get_obj")
    public Map<String, Object> obj(HttpServletRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("obj", service.selectOne(scopedSpec(request)));
        return success(result);
    }

    @RequestMapping("/get_list")
    public Map<String, Object> getList(HttpServletRequest request) {
        return success(service.selectToPage(scopedSpec(request)));
    }

    @RequestMapping("/list_group")
    public Map<String, Object> listGroup(HttpServletRequest request) {
        return success(service.selectGroupCount(scopedSpec(request)));
    }

    @RequestMapping("/bar_group")
    public Map<String, Object> barGroup(HttpServletRequest request) {
        return success(service.selectBarGroup(scopedSpec(request)));
    }

    @RequestMapping(value = {"/count_group", "/count"})
    public Map<String, Object> count(HttpServletRequest request) {
        return success(service.count(scopedSpec(request)));
    }

    @RequestMapping(value = {"/sum_group", "/sum"})
    public Map<String, Object> sum(HttpServletRequest request) {
        return success(service.sum(scopedSpec(request)));
    }

    @RequestMapping(value = {"/avg_group", "/avg"})
    public Map<String, Object> avg(HttpServletRequest request) {
        return success(service.avg(scopedSpec(request)));
    }

    public Map<String, Object> success(Object result) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", result);
        return response;
    }

    public Map<String, Object> error(Integer code, String message) {
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("code", code);
        detail.put("message", message);
        Map<String, Object> response = new HashMap<>();
        response.put("error", detail);
        return response;
    }

    private QuerySpec scopedSpec(HttpServletRequest request) {
        return service.scopeToAuthenticatedUser(service.parse(request), scopedUserId(request));
    }

    private QuerySpec scopedMutationSpec(HttpServletRequest request) {
        return service.scopeMutationToAuthenticatedUser(service.parse(request), scopedUserId(request));
    }

    private Integer scopedUserId(HttpServletRequest request) {
        return RequestUser.isAdmin(request) ? null : RequestUser.userIdOrNull(request);
    }
}
