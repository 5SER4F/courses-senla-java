package org.uhanov.transaction;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.uhanov.repository.TransactionalConnectHolder;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionAspect {
    private final TransactionalConnectHolder connectHolder;

    @Pointcut("@annotation(org.uhanov.transaction.Transaction)")
    public void testPointcut() {

    }

    @Before("testPointcut()")
    public void openTransaction() {
        connectHolder.openTransaction();
    }

    @AfterReturning("testPointcut()")
    public void closeTransaction() {
        connectHolder.commitTransaction();
    }

    @AfterThrowing("testPointcut()")
    public void rollbackTransaction() {
        connectHolder.rollbackTransaction();
    }


}
