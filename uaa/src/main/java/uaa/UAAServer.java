package uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author tanjunwen
 * @version 1.0
 * @Purpose uaa认证服务启动类
 * @Date 2022/5/12
 * */
@SpringBootApplication
@EnableEurekaClient
public class UAAServer {
    public static void main(String[] args) {
        SpringApplication.run(UAAServer.class);
    }
}
