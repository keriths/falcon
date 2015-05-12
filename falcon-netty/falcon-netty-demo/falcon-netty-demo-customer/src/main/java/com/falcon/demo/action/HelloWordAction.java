package com.falcon.demo.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.falcon.demo.api.HelloWordService;
import com.falcon.demo.api.dto.HelloDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */
@Controller
public class HelloWordAction {
    @Resource(name = "helloWordService")
    private HelloWordService helloWordService;
    @RequestMapping(value = "/ajax/{name:[a-z]+}")
    @ResponseBody
    public Object ajax_json(@PathVariable String name){
        HelloDTO d = new HelloDTO();
        List ll = new ArrayList();
        ll.add(d);
        Object l = helloWordService.hellolist(null);
        System.out.println("******"+l);
        return l.toString();
    }

    // /ajax/obj?name=aaa&val=aab
    @RequestMapping(value = "/ajax/obj")
    @ResponseBody
    public Object ajax_object(HelloDTO hello){
        return hello;
    }
    // /ajax/obj1?name=aaa&val=aab   不可行
    @RequestMapping(value = "/ajax/obj1")
    @ResponseBody
    public Object ajax_object1(@RequestParam(required = false) HelloDTO hello){
        return hello;
    }

    // /ajax/list?names=a&names=2
    @RequestMapping(value = "/ajax/list")
    @ResponseBody
    public Object ajax_list(@RequestParam(required = false) List<String> names){
        return names;
    }

    // /ajax/list1?names=a&names=2   此方式不可行
    @RequestMapping(value = "/ajax/list1")
    @ResponseBody
    public Object ajax_list1(List<String> names){
        return names;
    }

    // /ajax/list2?name=a&name=2
    @RequestMapping(value = "/ajax/list2")
    @ResponseBody
    public Object ajax_list2(@RequestParam(value =  "name",required = false) List<String> names){
        return names;
    }

    @RequestMapping(value = "/test/requestBody")
    @ResponseBody
    public Object requestBody(@RequestBody String jsonBody){
        JSONObject json = JSON.parseObject(jsonBody);
        return "aa:"+json.get("name");
    }

}
