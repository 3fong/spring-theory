package com.ll.annotation.conf;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liulei
 * @Description 异步请求
 *  1 支持异步处理 asyncSupported = true
 *  2 开启异步处理 HttpServletRequest.startAsync()
 *  3 业务逻辑进行异步处理;开始异步处理
 * @create 2022/3/31 21:28
 */
@WebServlet(value="/async",asyncSupported = true)
public class MyHttpServletSync extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(Thread.currentThread()+" doget start...."+ System.currentTimeMillis());
        AsyncContext startAsync = req.startAsync();
//        业务逻辑进行异步处理;开始异步处理
        startAsync.start(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread()+" sub thread start..." + System.currentTimeMillis());

                    saySleep();
                    // 声明处理完成
                    startAsync.complete();
                    // 获取异步上下文
                    AsyncContext context = req.getAsyncContext();
                    // 获取异步响应结果
                    ServletResponse response = context.getResponse();
                    response.getWriter().write("response...");
                } catch (IOException e) {
                } finally {
                    System.out.println(Thread.currentThread()+" sub thread end..."+ System.currentTimeMillis());
                }
            }
        });
        System.out.println(Thread.currentThread()+" doget end..."+ System.currentTimeMillis());
    }

    public void saySleep() {
        System.out.println("sleep..............");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
