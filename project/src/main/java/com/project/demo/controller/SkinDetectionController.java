package com.project.demo.controller;

import com.project.demo.entity.SkinDetection;
import com.project.demo.service.SkinDetectionService;
import com.project.demo.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;


/**
 * 皮肤检测：(SkinDetection)表控制层
 *
 */
@RestController
@RequestMapping("/skin_detection")
public class SkinDetectionController extends BaseController<SkinDetection, SkinDetectionService> {

    /**
     * 皮肤检测对象
     */
    @Autowired
    public SkinDetectionController(SkinDetectionService service) {
        setService(service);
    }
}
