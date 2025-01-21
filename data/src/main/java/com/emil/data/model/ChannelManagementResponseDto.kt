package com.emil.data.model

import com.emil.domain.model.ChannelManagementResponse
import com.emil.domain.model.ChannelType

class ChannelManagementResponseDto (val name: String="", val link: String?=null,
                                    val avatarUrl: String="", val description: String?=null,
                                    val type: ChannelType =  ChannelType.PRIVATE
)





      fun ChannelManagementResponseDto.toDomainModel ():ChannelManagementResponse{
          return ChannelManagementResponse(name, link, avatarUrl, description, type)
      }
