package com.falcon.util.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 18/10/28.
 */
public class TestDTO implements Serializable{
    public TestDTO(){}
    public TestDTO(String name,List<String> list){
        this.name=name;
        this.aaa=list;
    }
    private String name;
    private List<String> aaa;
    private Map<String,TestDTO> bbb;
    private String ccc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAaa() {
        return aaa;
    }

    public void setAaa(List<String> aaa) {
        this.aaa = aaa;
    }

    public Map<String, TestDTO> getBbb() {
        return bbb;
    }

    public void setBbb(Map<String, TestDTO> bbb) {
        this.bbb = bbb;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }
}
