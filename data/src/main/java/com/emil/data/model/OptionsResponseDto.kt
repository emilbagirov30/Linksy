package com.emil.data.model

import com.emil.domain.model.OptionResponse

class OptionResponseDto (val optionId:Long = 0, val optionText:String="", val selectedCount:Long=0)

     fun OptionResponseDto.toDomainModel():OptionResponse {
         return OptionResponse(optionId, optionText, selectedCount)
     }

     fun List<OptionResponseDto>.toDomainModelList ():List<OptionResponse>{
         return this.map { it.toDomainModel() }
     }