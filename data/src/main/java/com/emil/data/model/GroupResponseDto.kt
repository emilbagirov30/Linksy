package com.emil.data.model

import com.emil.domain.model.GroupResponse

class GroupResponseDto (val name:String="", val avatarUrl:String="")



  fun GroupResponseDto.toDomainModel():GroupResponse {
     return GroupResponse (name,avatarUrl)
  }