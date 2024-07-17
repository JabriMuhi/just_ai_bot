package com.just_ai.vk_bot.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class VkMessage @JsonCreator constructor(
    @JsonProperty("type") val type: String,
    @JsonProperty("object") val `object`: VkObject?
)

data class VkObject @JsonCreator constructor(
    @JsonProperty("message") val message: Message?,
    @JsonProperty("client_info") val client_info: ClientInfo?
)

data class Message @JsonCreator constructor(
    @JsonProperty("date") val date: Long,
    @JsonProperty("from_id") val from_id: Int,
    @JsonProperty("id") val id: Int,
    @JsonProperty("out") val out: Int,
    @JsonProperty("version") val version: Int,
    @JsonProperty("attachments") val attachments: List<Any>,
    @JsonProperty("conversation_message_id") val conversation_message_id: Int,
    @JsonProperty("fwd_messages") val fwd_messages: List<Any>,
    @JsonProperty("important") val important: Boolean,
    @JsonProperty("is_hidden") val is_hidden: Boolean,
    @JsonProperty("peer_id") val peer_id: Int,
    @JsonProperty("random_id") val random_id: Int,
    @JsonProperty("text") val text: String
)

data class ClientInfo @JsonCreator constructor(
    @JsonProperty("button_actions") val button_actions: List<String>,
    @JsonProperty("keyboard") val keyboard: Boolean,
    @JsonProperty("inline_keyboard") val inline_keyboard: Boolean,
    @JsonProperty("carousel") val carousel: Boolean,
    @JsonProperty("lang_id") val lang_id: Int
)

