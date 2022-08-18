package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.dao.OrderDetailDao;
import com.web.domain.OrderDetail;
import com.web.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
