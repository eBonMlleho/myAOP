package aop;

import aop.advice.After;
import aop.advice.Around;
import aop.advice.Before;
import aop.advice.PointCutting;
import java.lang.reflect.Proxy;

public class AOPExample {
    public static void main(String[] args) {
        EmployeeService es = (EmployeeService) Proxy.newProxyInstance(
                AOPExample.class.getClassLoader(),
                new Class[]{EmployeeService.class},
                new JdkAOPInvocationHandler(new EmployeeServiceImpl2(), new TestAspect())
        );

        int val = es.get();
        System.out.println();
        System.out.println("final returned result is： " + val);
    }
}

interface EmployeeService {
    int get();
    void print();
}

class EmployeeServiceImpl1 implements EmployeeService {
    @Override
    public int get() {
        System.out.println("this is EmployeeServiceImpl1 original method get()");
        return 5;
    }

    @Override
    public void print() {
        System.out.println("print");
    }
}



class EmployeeServiceImpl2 implements EmployeeService {
    @Override
    public int get() {
        System.out.println("this is EmployeeServiceImpl2 original method get()");
        return 10;
    }
    @Override
    public void print() {
        System.out.println("print");
    }
}

class EmployeeServiceImpl3 implements EmployeeService {
    @Override
    public int get() {
        System.out.println("this is EmployeeServiceImpl3 original method get()");
        return 15;
    }
    @Override
    public void print() {
        System.out.println("print");
    }
}


class TestAspect{
    @PointCutting(classArr = {EmployeeServiceImpl1.class})
    @Before
    public void beforePointCutting1(){
        System.out.println("this is EmployeeServiceImpl1 before_PointCutting 1 test");
    }

    @PointCutting(classArr = {EmployeeServiceImpl2.class})
    @Before
    public void beforePointCutting2(){
        System.out.println("this is EmployeeServiceImpl2 before_PointCutting 2 test");
    }

    @PointCutting(classArr = {EmployeeServiceImpl2.class, EmployeeServiceImpl3.class})
    @After
    public void afterPointCutting3(){
        System.out.println("this is EmployeeServiceImpl2 and EmployeeServiceImpl3 After_PointCutting test");
    }

    @After
    public void after1Fun() {
        System.out.println("this is after1111");
    }
    @After
    public void after2Fun() {
        System.out.println("this is after2222");
    }
    @Before
    public void before1Fun() {
        System.out.println("this is before1111");
    }

    @Before
    public void before2Fun() {
        System.out.println("this is before2222");
    }

    @Around
    public Object around1Fun(MethodInvocation mi) throws Throwable{
        System.out.println("-- -- --- this is around1111 before -------");
        Object res = mi.proceed();
        System.out.println("-- -- --- this is around1111 after -------");
        return res;
    }

    @Around
    public Object around2Fun(MethodInvocation mi) throws Throwable{
        System.out.println("-- -- --- this is around2222 before -------");
        Object res = mi.proceed();
        System.out.println("-- -- --- this is around2222 after -------");
        return res;
    }

}




/**
 *   option1:   @after / @around / @before
 *   option2:   @PointCut(EmployeeService.class),
 *              @AfterReturn(triggered when we get result)
 *              @AfterThrow(triggered when we get exception)
 */