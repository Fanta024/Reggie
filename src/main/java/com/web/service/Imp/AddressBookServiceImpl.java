package com.web.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.web.dao.AddressBookDao;
import com.web.domain.AddressBook;
import com.web.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements AddressBookService {

}
