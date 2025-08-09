package com.selena.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration        // Đây là 1 lớp cấu hình Spring
@EnableAsync          // Bật tính năng chạy bất đồng bộ cho toàn app
public class AsyncConfig {
    @Bean(name="asyncExecutor") // Đăng ký 1 bean tên "asyncExecutor"
    public TaskExecutor taskExecutor() { // Kiểu trả về là TaskExecutor (chuẩn Spring)

//        ThreadPoolTaskExecutor: quy định bao nhiêu thread (luồng) chạy song song và độ dài hàng đợi cho các tác vụ bất đồng bộ.
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//      số luồng cơ bản luôn ready.
//      Thiết lập core pool size = 5: hệ thống luôn giữ ít nhất 5 thread sẵn sàng xử lý task.
        taskExecutor.setCorePoolSize(5);
//      khi có nhiều task hơn, pool có thể mở rộng tối đa đến 10 luồng.
//      Thiết lập max pool size = 10: khi workload tăng (queue đầy), thread pool có thể mở rộng đến tối đa 10 thread.
        taskExecutor.setMaxPoolSize(10);
 //       Thiết lập queue capacity = 100: nếu tất cả core threads (5) đang bận, các task mới sẽ được đẩy vào hàng đợi (internal queue) chứa tối đa 100 task chờ xử lý.
   //     Khi queue đầy và chưa đến maxPoolSize, pool sẽ sinh thêm thread cho đến 10. Nếu queue đầy và đã có 10 thread, các task sau cùng bị từ chối (RejectedExecution).
        taskExecutor.setQueueCapacity(100);

//        Phương thức của InitializingBean, khởi tạo các thành phần bên trong executor sau khi đã set hết thuộc tính.
//        Gọi xong mới chắc chắn thread pool đã ready để dùng.
        taskExecutor.afterPropertiesSet();
//        Trả về bean ThreadPoolTaskExecutor đã cấu hình.
        return taskExecutor;
    }
}

