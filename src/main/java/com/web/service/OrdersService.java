package com.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.domain.Orders;

public interface OrdersService extends IService<Orders> {
     void submit(Orders orders);

     void updateStatus(Orders orders);
}
