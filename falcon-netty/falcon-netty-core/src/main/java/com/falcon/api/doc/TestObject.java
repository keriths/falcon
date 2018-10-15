package com.falcon.api.doc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 18/9/26.
 */
public class TestObject {
    private TestObjectDTO ttt;
    public TestObject testObject(TestObjectDTO testObjectDTO,Date beginDate,Date endDate,int in,Integer integer){
        return null;
    }

    public TestObjectDTO getTtt() {
        return ttt;
    }

    public void setTtt(TestObjectDTO ttt) {
        this.ttt = ttt;
    }

    public static class TestObjectDTO{
        private Date date;
        private BigDecimal bigDecimal;
        private String string;
        private Integer integer;
        private int anInt;
        private List<String> stringList;
        private Map<String,Map<String,List<Date>>> dateMap;
    }
}
