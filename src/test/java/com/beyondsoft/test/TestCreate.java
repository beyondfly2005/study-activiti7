package com.beyondsoft.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

public class TestCreate {

    //使用默认方式创建mysql相关的表

    @Test
    public void testCreateDbTable(){
        //使用activiti提供的工具类
        //创建processEngine的时候，就会自动创建表

        //默认方式
        /*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.get
        System.out.println(processEngine);
        */

        // 使用一般方式/自定义方式
        //配置文件的名字可以自定义
        //先构建ProcessEngineConfiguration
//        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //bean的名字也可以自定义
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        // 使用classpath下的activiti.cfg.xml中的配置创建processEngine
        //获取流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);


    }
}
