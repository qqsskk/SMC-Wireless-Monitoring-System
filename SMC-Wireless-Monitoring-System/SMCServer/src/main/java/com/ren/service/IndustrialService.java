package com.ren.service;

import com.ren.bean.IndustrialMessage;
import com.ren.dao.IndustrialMessageMapper;
import com.ren.dao.countIndustrialMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustrialService {

    @Autowired
    IndustrialMessageMapper industrialMessageMapper;
    @Autowired
    countIndustrialMessageMapper countIndustrialMessageMapper;

    public List checkIndustrialMessage(){
        List<IndustrialMessage> list = industrialMessageMapper.selectIndustrialMessage();
        return list;
    }
    public int selectIndustrialMessage(){
        return countIndustrialMessageMapper.countIndustrialMessage();
    }
}
