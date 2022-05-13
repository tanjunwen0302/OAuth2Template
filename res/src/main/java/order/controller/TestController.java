package order.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/test01")
    //拥有p1权限才可访问
    @PreAuthorize("hasAuthority('p1')")
    public JSONObject test01() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", "请求成功");
        return jsonObject;
    }
}
