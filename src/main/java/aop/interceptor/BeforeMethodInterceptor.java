package aop.interceptor;

import aop.MethodInvocation;

import java.lang.reflect.Method;

public class BeforeMethodInterceptor implements MethodInterceptor{

    private Object aspectObj;
    private Method aspectMethod;

    public BeforeMethodInterceptor(Object aspectObj, Method aspectMethod) {
        this.aspectObj = aspectObj;
        this.aspectMethod = aspectMethod;
    }

    @Override
    public Object invoke(MethodInvocation mi)  throws Throwable{
        aspectMethod.setAccessible(true);
        aspectMethod.invoke(aspectObj);
        return mi.proceed();
    }
}
