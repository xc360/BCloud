package com.xc.basic;

import com.xc.basic.service.BasicAuthorizeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BasicServerApplicationTests {

    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @Test
    public void contextLoads() {
//        PagingDto<OvertAppDto> pagingDto = appService.getOvertAppPage(1, null, new PagingBean(), new AppEntity());
//        System.out.println("111");


        basicAuthorizeService.deleteUserToken("1");
    }

}
