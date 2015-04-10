package com.falcon.demo.action;

import com.falcon.demo.api.HelloWordService;
import com.falcon.demo.api.dto.HelloDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */
@Controller
public class HelloWordAction {
    //@Resource(name = "helloWordService")
    //private HelloWordService helloWordService;
    @RequestMapping(value = "/ajax/{name:[a-z]+}")
    @ResponseBody
    public Object ajax_json(@PathVariable String name){

        return name;
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
}
