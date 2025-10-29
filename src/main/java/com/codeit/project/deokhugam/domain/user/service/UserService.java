package com.codeit.project.deokhugam.domain.user.service;

import com.codeit.project.deokhugam.domain.user.dto.request.PowerUserQueryParams;
import com.codeit.project.deokhugam.domain.user.dto.request.UserLoginRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserRegisterRequest;
import com.codeit.project.deokhugam.domain.user.dto.request.UserUpdateRequest;
import com.codeit.project.deokhugam.domain.user.dto.response.UserDto;
import com.codeit.project.deokhugam.global.common.dto.PageResponse;

public interface UserService {

  UserDto create(UserRegisterRequest request);

  UserDto login(UserLoginRequest request);

  UserDto findById(String userId);

  void softDelete(String userId);

  UserDto update(String id, UserUpdateRequest request);

  void hardDelete(String userId);

  PageResponse powerList(PowerUserQueryParams params);
}
