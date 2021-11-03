package aop.interceptor;

import aop.MethodInvocation;

public interface MethodInterceptor {
    Object invoke(MethodInvocation mi) throws Throwable;
}
