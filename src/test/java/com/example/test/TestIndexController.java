package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.concurrent.CountDownLatch;

import com.example.controller.IndexController;
import com.example.controller.RoomController;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

public class TestIndexController {
    @Test
    public void testIndexPage() throws Exception {
        IndexController indexController = new IndexController();
        // 搭建模拟MVC
        MockMvc mockMvc = standaloneSetup(indexController).build();
        // 对"/"执行GET请求，预期得到home视图
        mockMvc.perform(get("/")).andExpect(view().name("index"));
    }

    @Test
    public void testRoomPage() throws Exception {
        // MockServletContext mockServletContext = new MockServletContext();
        // ServletContext servletContext = mock(ServletContext.class);
        // ServletContextEvent sce = mock(ServletContextEvent.class);
        // OnLineListener onLineListener = new OnLineListener();
        // onLineListener.contextInitialized(sce);
        RoomController roomController = new RoomController();
        // 搭建模拟MVC
        MockMvc mockMvc = standaloneSetup(roomController).build();
        mockMvc.perform(get("/room/enterRoom?roomID=1")).andExpect(view().name("room"));
    }

    @Test
    public void testHttpClientGet() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/study_with_me/room/enterRoom?roomID=1");
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            Thread.sleep(1000);
            httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/leaveRoom?roomID=1"));
        }
        httpClient.close();
    }

    @Test
    public void clearPeople() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        for (int i = 0; i < 3000; ++i) {
            httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/leaveRoom?roomID=1"));
        }
        httpClient.close();
    }

    // 高并发模拟：1000个线程并发访问自习室，查看自习室人数是否正确(非人工查看，而是获取响应体中的数据)
    // 不知道为什么加不加同步块，都可能会发生下面错误
    // org.apache.http.conn.HttpHostConnectException: Connect to localhost:8080 [localhost/127.0.0.1,
    // localhost/0:0:0:0:0:0:0:1] failed: Connection refused: connect
    @Test
    public void testHighConcurrency() throws Exception {
        int num = 100;
        MyThread myThread = new MyThread();

        for (int i = 0; i < num; ++i) {
            // CloseableHttpClient httpClient = HttpClients.createDefault();
            // java.lang.IllegalStateException: Connection pool shut down
            // 需要使用连接池
            // httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/enterRoom?roomID=1"));
            new Thread(myThread).start();
            // httpClient.close();
        }

        // startTaskAllInOnce(num, myThread);

        System.out.println("已全部进入");
        Thread.sleep(5 * 1000);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // CloseableHttpResponse response =
        // httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/peopleCount?roomID=1"));
        // assertEquals(response.getStatusLine().getStatusCode(), 200);
        // assertEquals(EntityUtils.toString(response.getEntity(), "UTF-8"), Integer.toString(num));
        // 恢复，离开教室
        for (int i = 0; i < num; ++i) {
            httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/leaveRoom?roomID=1"));
        }
        httpClient.close();
        System.out.println("已全部离开");

    }

    public static void startTaskAllInOnce(int threadNums, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        for (int i = 0; i < threadNums; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        // 使线程在此等待，当开始门打开时，一起涌入门中
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            // 将结束门减 1，减到 0 时，就可以开启结束门了
                            endGate.countDown();
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            t.start();
        }
        // 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
    }

}

// 单独是只为了获取一次getHttpClient
class MyThread implements Runnable {
    CloseableHttpClient httpClient = HttpClientPoolUtil.getHttpClient();

    @Override
    public void run() {
        enterRoom(httpClient);
    }

    public static void enterRoom(CloseableHttpClient httpClient) {
        // System.out.println("----------->" + Thread.currentThread().getName() + "进入自习室");;
        try {
            httpClient.execute(new HttpGet("http://localhost:8080/study_with_me/room/enterRoom?roomID=1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}