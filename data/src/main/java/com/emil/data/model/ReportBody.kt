package com.emil.data.model

import com.emil.domain.model.ReportRequest

data class ReportBody (val userId:Long? =null,val channelId:Long?=null,val reason:String="F")



 fun ReportBody.toDomainModel(reportRequest: ReportRequest):ReportBody{
     return ReportBody(reportRequest.userId,reportRequest.channelId,reportRequest.reason)
 }