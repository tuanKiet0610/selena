package com.selena.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ControllerLoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerLoggingAspect.class);

//  tất cả các class trong controller
    @Around("execution(* com.selena.controller..*(..))")
    public Object monitorTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        thời gian bắt đầu
        long start = System.currentTimeMillis();
//      ghi log Start
        LOGGER.info("Start: {}.{}"
//                    lấy tên đầy đủ của class chứa method đang chạy
                    ,joinPoint.getStaticPart().getSignature().getDeclaringTypeName()
//                    lấy ra tên của method đang chạy
                    ,joinPoint.getStaticPart().getSignature().getName());

        try{
//          cho phép method gốc chạy tiếp
            Object result = joinPoint.proceed();

//          thời gian kết thúc
            long end = System.currentTimeMillis();

//          ghi log kết thúc kèm theo tên của class.method đang chạy và thời gian chạy của method đó
            LOGGER.info("End: {}.{} success and Time taken= {} ms"
                        ,joinPoint.getStaticPart().getSignature().getDeclaringTypeName()
                        ,joinPoint.getSignature().getName()
                        ,(end - start));
            return result;

        }catch (Throwable e){
//          ghi log lỗi chi tiết để dễ fix hơn kèm theo tên class.method lỗi và message lỗi
            LOGGER.info("Error: {}.{} message={}"
                        ,joinPoint.getStaticPart().getSignature().getDeclaringTypeName()
                        ,joinPoint.getStaticPart().getSignature().getName()
                        ,e.getMessage());
            throw e;
        }
    }
}
