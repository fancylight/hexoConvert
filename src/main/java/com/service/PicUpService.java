package com.service;

import com.exception.CommonException;

public interface PicUpService {
    String upLoad(byte[] pic, String fileName) throws CommonException;
}
