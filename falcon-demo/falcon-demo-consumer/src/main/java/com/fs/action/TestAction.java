package com.fs.action;

import com.alibaba.fastjson.JSONObject;
import com.fs.api.demo.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by fanshuai on 15-1-12.
 */
@Controller
public class TestAction {

    @Resource(name="productServicenetty")
    public ProductService productService;

    @RequestMapping(value="/index",method= RequestMethod.GET)
    @ResponseBody
    public Object index(){
        JSONObject json = new JSONObject();
        json.put("success",true);
        json.put("product_name",productService.produce("XXXProduct"));
        return json;
    }
}
