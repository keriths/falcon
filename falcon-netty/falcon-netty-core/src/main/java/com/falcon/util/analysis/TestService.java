package com.falcon.util.analysis;

import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by fanshuai on 18/10/28.
 */
public class TestService {

    public Map<String,TestDTO> maplist(List<TestDTO> testDTOList){
        if (CollectionUtils.isEmpty(testDTOList)){
            return null;
        }
        Map<String,TestDTO> testDTOMap = new HashMap<String, TestDTO>();
        for (TestDTO testDTO:testDTOList){
            testDTOMap.put(testDTO.getName(),testDTO);
        }
        return testDTOMap;
    }


}
