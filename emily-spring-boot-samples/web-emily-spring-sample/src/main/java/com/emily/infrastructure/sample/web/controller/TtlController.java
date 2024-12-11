package com.emily.infrastructure.sample.web.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.TtlWrappers;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.emily.infrastructure.sample.web.mapper.mysql.MysqlMapper;
import com.emily.infrastructure.tracing.holder.TracingWrapper;
import com.emily.infrastructure.tracing.holder.LocalContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * TTL测试控制器
 *
 * @author Emily
 * @since 2021/05/17
 */
@RestController
@RequestMapping("api/ttl/")
public class TtlController {

    @Autowired
    private MysqlMapper mysqlMapper;

    public static void getParentChild() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        context.set("set parent value");
        Runnable ttlRunnable = TtlRunnable.get(new Runnable() {
            @Override
            public void run() {
                //读取父线程中的值，其值为：set parent value
                System.out.println(context.get());
            }
        });
        executorService.submit(ttlRunnable);
        //读取当前线程中的值，其值为：set parent value
        System.out.println(context.get());
    }

    public static void getParentChildCall() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        context.set("set parent value");
        Callable<String> ttlCallable = TtlCallable.get(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //读取父线程中的值，其值为：set parent value
                System.out.println(context.get());
                return "emily";
            }
        });
        Future<String> future = executorService.submit(ttlCallable);
        //读取当前线程中的值，其值为：set parent value
        System.out.println(context.get());
        System.out.println(future.get());
    }

    public static void getParentChildPool() throws ExecutionException, InterruptedException {
        ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        context.set("set parent value");
        Callable<String> ttlCallable = TtlCallable.get(new Callable<String>() {
            @Override
            public String call() throws Exception {
                //读取父线程中的值，其值为：set parent value
                System.out.println(context.get());
                return "emily";
            }
        });
        Future<String> future = executorService.submit(ttlCallable);
        //读取当前线程中的值，其值为：set parent value
        System.out.println(context.get());
        System.out.println(future.get());
    }

    @GetMapping("test")
    public String get() throws ExecutionException, InterruptedException {
        System.out.println("-----上下文ID:" + LocalContextHolder.current().getTraceId());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(TtlWrappers.wrapSupplier(() -> {
            System.out.println("子线程1：" + Thread.currentThread().getName() + ":" + LocalContextHolder.current().getTraceId());
            return LocalContextHolder.current().getTraceId();
        }));
        return future1.get();
    }

    @GetMapping("context")
    public void context() {
        TracingWrapper.run(null, () -> {
            mysqlMapper.getMysql("sd", "sdf");
            mysqlMapper.getMysql("田晓霞", "520");
        });
    }
}
