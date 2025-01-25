package com.emil.domain.model

data class UserPageDataResponse(val username:String, val link:String?, val avatarUrl:String, val birthday:String?, val isSubscriber:Boolean, val subscriptionsCount:Long,
                                val subscribersCount:Long,
                                var isPageOwnerBlockedByViewer:Boolean,val messageMode: MessageMode,
                                val isSubscription:Boolean,val confirmed:Boolean,val online:Boolean,val lastActive:String)