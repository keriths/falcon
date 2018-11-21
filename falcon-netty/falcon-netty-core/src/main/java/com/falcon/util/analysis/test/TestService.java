package com.falcon.util.analysis.test;

import com.falcon.util.analysis.annotation.DESC;
import com.falcon.util.analysis.annotation.MethodID;
import com.falcon.util.analysis.annotation.ServiceID;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fanshuai on 18/10/28.
 */
@DESC("测试服务")
@ServiceID("testService")
public class TestService {
    @DESC("maplist测试方法")
    @MethodID("maplist")
    public Map<String,Object> maplist(@DESC("list类型参数")List<TestDTO> testDTOList,String str,int i,long l,BigDecimal b,Long ll,Date date){
        if (CollectionUtils.isEmpty(testDTOList)){
            return null;
        }
        Map<String,Object> testDTOMap = new HashMap<String, Object>();
//        for (TestDTO testDTO:testDTOList){
//            testDTOMap.put(testDTO.getName(),testDTO);
//        }
        testDTOMap.put("testDTOList",testDTOList);
        testDTOMap.put("str",str);
        testDTOMap.put("i",i);
        testDTOMap.put("l",l);
        testDTOMap.put("b",b);
        testDTOMap.put("ll",ll);
        testDTOMap.put("date",date);
        return testDTOMap;
    }
}
