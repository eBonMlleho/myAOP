package aop;


import aop.advice.*;
import aop.interceptor.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JdkAOPInvocationHandler implements InvocationHandler {

    private Object originObj;
    private Object aspectObj;

    public JdkAOPInvocationHandler(Object originObj, Object aspectObj) {
        this.originObj = originObj;
        this.aspectObj = aspectObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> aspectClass = aspectObj.getClass();
        List<MethodInterceptor> interceptors = new ArrayList<>();
        List<MethodInterceptor> afterThrowInterceptors = new ArrayList<>();
        List<MethodInterceptor> afterReturnInterceptors = new ArrayList<>();

        for(Method aspectMethod: aspectClass.getDeclaredMethods()) {
            boolean pointCuttingContainedOriginalObj = false;
            interloop: for(Annotation ano: aspectMethod.getDeclaredAnnotations()) {
                MethodInterceptor methodInterceptor = null;
                if(ano.annotationType() == PointCutting.class){ // check if pointCutting annotation
                    PointCutting currAnnotation = (PointCutting) ano;
                    // check whether value includes originObj
                    for(Class clazz : currAnnotation.classArr()){
                        if(clazz == originObj.getClass()){
                            pointCuttingContainedOriginalObj = true;
                            continue interloop; // continue Before/After/Around Annotation
                        }
                    }
                    if(!pointCuttingContainedOriginalObj){ break interloop; } //Try next method

                }else{  // continue Before/After/Around Annotation
                    if(ano.annotationType() == Before.class) {
                        methodInterceptor = new BeforeMethodInterceptor(aspectObj, aspectMethod);
                    } else if(ano.annotationType() == After.class) {
                        methodInterceptor = new AfterMethodInterceptor(aspectObj, aspectMethod);
                    } else if(ano.annotationType() == Around.class) {
                        methodInterceptor = new AroundMethodInterceptor(aspectObj, aspectMethod);
                    } else if(ano.annotationType() == AfterThrow.class) {
                        afterThrowInterceptors.add(new AfterMethodInterceptor(aspectObj, aspectMethod));
                    } else if(ano.annotationType() == AfterReturn.class){
                        afterReturnInterceptors.add(new AfterReturnMethodInterceptor(aspectObj, aspectMethod));
                    }
                }
                interceptors.add(methodInterceptor);
            }
        }
        MethodInvocation mi = new ProxyMethodInvocation(interceptors, originObj, method, args);
        //  return mi.proceed();

        try{  //  if(exception) -> execute afterThrow
            Object result = mi.proceed();
            new ProxyMethodInvocation(afterReturnInterceptors, originObj, method, args).proceed();
            return result;
        } catch(Exception e){ // no exception, execute afterReturn
            return new ProxyMethodInvocation(afterThrowInterceptors, originObj, method, args).proceed();
        }
    }
}
