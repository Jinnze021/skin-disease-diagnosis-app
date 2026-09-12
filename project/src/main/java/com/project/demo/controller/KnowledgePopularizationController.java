package com.project.demo.controller;

import com.project.demo.entity.KnowledgePopularization;
import com.project.demo.service.KnowledgePopularizationService;
import com.project.demo.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;


/**
 * 知识科普：(KnowledgePopularization)表控制层
 *
 */
@RestController
@RequestMapping("/knowledge_popularization")
public class KnowledgePopularizationController extends BaseController<KnowledgePopularization, KnowledgePopularizationService> {

    /**
     * 知识科普对象
     */
    @Autowired
    public KnowledgePopularizationController(KnowledgePopularizationService service) {
        setService(service);
    }



    @PostMapping("/add")
    @Transactional
    public Map<String, Object> add(HttpServletRequest request) throws IOException {
        Map<String,Object> paramMap = service.readBody(request.getReader());
        this.addMap(paramMap);
        return success(1);
    }


}
