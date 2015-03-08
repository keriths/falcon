package com.fs.falcon.server.hessian;

/**
 * Created by fanshuai on 15-1-13.
 */
public class ProviderConfig {
    /**
     * 服务接口
     */
    private Class serviceInterface;
    /**
     * 提供服务接口的项目名,默认取web.xml的display-name
     */
    private String domain;
    /**
     * 服务提供者的IP
     */
    private String ip;
    /**
     * 服务提供者端口
     */
    private Integer port;
    /**
     * 权重，默认1
     */
    private Integer weight=1;
    /**
     * 服务分组，默认default
     */
    private String group="default";
    /**
     * 服务版本默认1.0
     */
    private String version="1.0";
    /**
     * 部署的路径，自动取
     */
    private String serverContextName;
    /**
     * 此服务的访问路径
     */
    private String serverPath;

    public ProviderConfig(){}
    public ProviderConfig(String domain,Class serviceInterface,String ip,Integer port,String serverContextName,String serverPath){
        this.domain = domain;
        this.serviceInterface = serviceInterface;
        this.ip = ip;
        this.port = port;
        this.serverContextName=serverContextName;
        this.serverPath = serverPath;
    }

    public String getHesianUrl(){
        return "http://"+ip+":"+port+serverContextName+serverPath;
    }

    public String getZookeeperServerNodeName(){
        return domain+"_"+serviceInterface.getName();
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerContextName() {
        return serverContextName;
    }

    public void setServerContextName(String serverContextName) {
        this.serverContextName = serverContextName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }



    public Class getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
}
