package com.beyondsoft.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key 到Activiti
     */
    @Test
    public void addBusinessKey(){
        //1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3、启动流程的过程中 添加businessKey
        // 第一个参数 是流程定义的key 第二个参数是 businessKey
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvecton", "1001");
        //4、输出
        System.out.println(instance.getBusinessKey());
    }
}
