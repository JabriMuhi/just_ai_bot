package com.just_ai.vk_bot.services

import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.ThreadLocalRandom

@Service
class VkBotService(
    private val restTemplate: RestTemplate,
    private val dotenv: Dotenv
) {

    private val accessToken: String = dotenv["VK_ACCESS_TOKEN"]
    private val logger = LoggerFactory.getLogger(VkBotService::class.java)

    fun sendMessage(userId: Int, message: String) {
        val randomId = ThreadLocalRandom.current().nextInt()
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

        logger.info("Generated URL: $url")

        retry(3) {
            val response = restTemplate.postForObject(url, null, String::class.java)
            logger.info("VK API response: $response")
        }
    }

    private fun <T> retry(times: Int, block: () -> T): T {
        repeat(times - 1) {
            try {
                return block()
            } catch (e: HttpClientErrorException) {
                logger.warn("VK API call failed, not retrying... (${e.message})")
                throw e
            } catch (e: Exception) {
                logger.warn("VK API call failed, retrying... (${e.message})")
            }
        }
        return block()
    }
}
