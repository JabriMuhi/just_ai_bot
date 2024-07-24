package com.just_ai.vk_bot.controllers

import com.just_ai.vk_bot.models.VkMessage
import com.just_ai.vk_bot.services.VkBotService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vk")
class VkBotController(private val vkBotService: VkBotService) {

    private val logger = LoggerFactory.getLogger(VkBotController::class.java)

    @PostMapping
    fun handleMessage(@RequestBody message: VkMessage): ResponseEntity<String> {

        // Подтверждение url ботом
        if (message.type == "confirmation") {
            return ResponseEntity.ok("cf1856f5")
        }

        if (message.type == "message_new") {
            val vkMessage = message.`object`?.message
            if (vkMessage != null) {
                val text = vkMessage.text
                val userId = vkMessage.from_id

                logger.info("Received new message from user $userId: $text")

                try {
                    vkBotService.sendMessage(userId, "Вы сказали: $text")
                    logger.info("Sent message to user $userId: Вы сказали: $text")
                } catch (e: Exception) {
                    logger.error("Failed to send message to user $userId: ${e.message}")
                }
            } else {
                logger.warn("Message object is null or invalid")
            }
        }

        return ResponseEntity.ok("ok")
    }
}