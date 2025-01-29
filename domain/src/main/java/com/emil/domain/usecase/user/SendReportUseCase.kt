package com.emil.domain.usecase.user

import com.emil.domain.model.ReportRequest
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class SendReportUseCase (private val peopleRepository: PeopleRepository){
    suspend fun execute (token:String,report: ReportRequest):Response<Unit>{
        return peopleRepository.sendReport(token, report)
    }
}