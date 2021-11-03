# myAOP

implemented: \
  @PointCut(EmployeeService.class), \
  @AfterReturn(triggered when we get result)
  @AfterThrow(triggered when we get exception)

Test:
  run main() in AOPExample and change the proxy parameters accordingly:  
  JdkAOPInvocationHandler(new ClassName(), new TestAspect())

  PointCutting: 
    afterPointCutting3() allows EmployeeServiceImpl2.class, EmployeeServiceImpl3.class;
    beforePointCutting2() allows EmployeeServiceImpl2.class;
    beforePointCutting1() allows EmployeeServiceImpl1.class;
    
    JdkAOPInvocationHandler(new new EmployeeServiceImpl2(), new TestAspect()) --> beforePointCutting2() and afterPointCutting3() will be execcuted.
