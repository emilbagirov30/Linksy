package com.emil.domain.model

import okhttp3.MultipartBody

class GroupEditData  (val groupId:Long,val name:String, val oldAvatarUrl:String?,val avatar: MultipartBody.Part?)