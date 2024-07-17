package com.just_ai.vk_bot.services

import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Service
class VkBotService(
    private val restTemplate: RestTemplate,
    private val dotenv: Dotenv
) {

    private val accessToken = dotenv["VK_ACCESS_TOKEN"]
    private val logger = LoggerFactory.getLogger(VkBotService::class.java)
    fun sendMessage(userId: Int, message: String) {
        val randomId = UUID.randomUUID().hashCode()
        logger.info("Sending message to user $userId: $message")

        val url = UriComponentsBuilder.fromHttpUrl("https://api.vk.com/method/messages.send")
            .queryParam("user_id", userId)
            .queryParam("message", message)
            .queryParam("random_id", randomId)
            .queryParam("access_token", accessToken)
            .queryParam("v", "5.199")
            .build()
            .encode()
            .toUri()

        val response = restTemplate.postForObject(url, null, String::class.java)
        logger.info("VK API response: $response")
    }
}
