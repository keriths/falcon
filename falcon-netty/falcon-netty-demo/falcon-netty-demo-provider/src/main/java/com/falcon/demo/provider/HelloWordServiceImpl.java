package com.falcon.demo.provider;

import com.falcon.demo.api.HelloWordService;
import com.falcon.demo.api.dto.HelloDTO;
import com.falcon.util.analysis.annotation.DESC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */
@DESC("helloWord测试服务")
public class HelloWordServiceImpl implements HelloWordService {
    @Override
    @DESC("sayHello方法")
    public String sayHello(@DESC("名称") String name) {
        try {
            Thread.sleep(100l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello : "+name;
    }

    @Override
    @DESC("hello方法")
    public HelloDTO hello(@DESC("hello实体")HelloDTO helloDTO) {
        try {
            HelloDTO helloDTO1 = new HelloDTO();
            helloDTO1.setName("hello : ");
            return helloDTO1;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @DESC(value = "hellolist")
    public List<HelloDTO> hellolist(@DESC("hello实体列表")List<HelloDTO> helloDTOs) {
        try {
            HelloDTO helloDTO1 = new HelloDTO();
            helloDTO1.setB(new BigDecimal("1212"));
            helloDTO1.setName("hello : alksfjaslkfjasklfjasklfjasklfjasklfjasklfjasklfjakslfjasjhfasfasljfalsdjfalksdfjasklfjakslfdjaslkfjaslkfjasklfjaskldfjasklfjasklfjsaklfjasklfjasklfjasklfjasklfjasklfjasklfjasklfjaskldfjaskldfjaskldfj");
            List<HelloDTO> l = new ArrayList<HelloDTO>();
            l.add(helloDTO1);
            return l;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
