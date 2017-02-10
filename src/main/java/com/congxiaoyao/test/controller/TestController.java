package com.congxiaoyao.test.controller;

import com.congxiaoyao.test.pojo.TestPO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jaycejia on 2016/12/8.
 */
@RestController
public class TestController {
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public TestPO echo(@RequestBody TestPO testPO) {
        return testPO;//echo
    }
}
