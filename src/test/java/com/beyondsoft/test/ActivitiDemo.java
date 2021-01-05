package com.beyondsoft.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

    /**
     * 测试流程部署
     */
    @Test
    public void testDeploy(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepisitoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、使用service进行流程的部署 定义一个流程的名字，把bpmn和png部署到数据中
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png").deploy();
        //4、输出部署信息（非必须）
        System.out.println("流程部署id="+deployment.getId());
        System.out.println("流程部署id="+deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcess(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3、
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        //4、输出内容
        System.out.println("流程定义ID="+instance.getProcessDefinitionId());
        System.out.println("流程实例ID="+instance.getId());
        System.out.println("当前活动的ID="+instance.getActivityId());
    }

    /**
     * 查询个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3、根据流程的key和任务的负责人查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") //流程的key
                .taskAssignee("zhangsan")  //流程的负责人
                .list();
        for (Task task : list) {
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务的id："+task.getId());
            System.out.println("任务的负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        //根据任务id完成任务
        taskService.complete("2505");
    }

    //完成jerry的任务 经理审批
    @Test
    public void completeTask4Jerry(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        //根据任务id完成任务
        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
                .taskAssignee("jerry").singleResult();
        taskService.complete(task.getId());
    }

    //完成jack的任务 总经理审批
    @Test
    public void completeTask4Jack(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        //根据任务id完成任务
        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
                .taskAssignee("jack").singleResult();
        taskService.complete(task.getId());
    }

    //完成rose的任务 财务审批
    @Test
    public void completeTask4Rose(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取taskService
        TaskService taskService = processEngine.getTaskService();
        //根据任务id完成任务
        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
                .taskAssignee("rose").singleResult();
        taskService.complete(task.getId());
    }

    //使用压缩包zip方式部署
    public void deployProcessByZip(){
        //1、创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/evection.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署的名称="+deploy.getName());

    }

    //查询流程定义
    @Test
    public void queryProcessDefinition(){
        //1、获取流程引擎processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService
        RepositoryService repositoryService =processEngine.getRepositoryService();
        //获取ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询当前所有的流程定义
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myEvection").orderByProcessDefinitionVersion().desc().list();
        //输出信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义id="+processDefinition.getId());
            System.out.println("流程定义名称="+processDefinition.getName());
            System.out.println("流程定义key="+processDefinition.getKey());
            System.out.println("流程定义版本="+processDefinition.getVersion());
            System.out.println("流程部署id="+processDefinition.getDeploymentId());
        }
    }

    //删除流程部署信息
    @Test
    public void deleteDeployment(){
        //1、获取流程引擎processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //通过流程部署id
        String deploymentId ="1";
        repositoryService.deleteDeployment(deploymentId);
        repositoryService.deleteDeployment(deploymentId,true); //级联删除 未完成的任务也一起删除
    }

    //下载 资源文件
    // 方案1 使用activiti提供的api 下载资源文件
    @Test
    public void getDeployment() throws IOException {
        //1、获取流程引擎processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、获取ProcessDefinitionQuery对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("myEvection")
                .singleResult();
        //根据流程定义信息 获取部署id
        String deploymentId = processDefinition.getDeploymentId();
        //5、通过repositoryService 传递部署id参数 读取资源信息 png 和bpmn
        //5.1获取png的流
        String pngName = processDefinition.getDiagramResourceName();
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        //5.2获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        //6、构造OutputStream流
        File pngFile = new File("d:/evectionflow01.png");
        File bpmnFile = new File("d:/evectionflow01.bpmn");
        FileOutputStream pngOutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        IOUtils.copy(pngInput,pngOutStream);
        IOUtils.copy(bpmnInput,bpmnOutputStream);
        //8 关闭流
        pngOutStream.close();
        bpmnOutputStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    //查看历史信息
    @Test
    public void findHistoryInfo(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        //查询act_inst表
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        instanceQuery.processInstanceId("2501");
        //增加排序操作， 按开始时间升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = instanceQuery.list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getProcessDefinitionId());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("=================");
        }
    }
}
