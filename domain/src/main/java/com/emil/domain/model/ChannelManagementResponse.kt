package com.emil.domain.model;

data class ChannelManagementResponse (val name: String, val link: String?,
                                       val avatarUrl: String, val description: String?,
                                       val type: ChannelType)
