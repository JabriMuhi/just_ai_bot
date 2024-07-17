package com.just_ai.vk_bot

import com.just_ai.vk_bot.services.VkBotService
import io.github.cdimascio.dotenv.Dotenv
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.web.client.RestTemplate
import java.net.URI
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class VkBotServiceTest {

    private lateinit var vkBotService: VkBotService

    @Mock
    private lateinit var restTemplate: RestTemplate

    private val dotenv = mock<Dotenv> {
        on { get("VK_ACCESS_TOKEN") } doReturn "dummy_token"
    }

    @BeforeEach
    fun setUp() {
        vkBotService = VkBotService(restTemplate, dotenv)
    }

    @Test
    @DisplayName("sendMessage should send message")
    fun sendMessageIsCorrect() {
        val userId = 123
        val message = "Hello!"

        vkBotService.sendMessage(userId, message)

        val uriCaptor = argumentCaptor<URI>()
        val requestCaptor = argumentCaptor<Any>()
        val responseTypeCaptor = argumentCaptor<Class<*>>()

        verify(restTemplate).postForObject(
            uriCaptor.capture(),
            requestCaptor.capture(),
            responseTypeCaptor.capture()
        )

        val expectedUri = "https://api.vk.com/method/messages.send?user_id=123&message=Hello!&random_id="
        assert(uriCaptor.firstValue.toString().startsWith(expectedUri))
        assertEquals<Any?>(null, requestCaptor.firstValue)
        assertEquals(String::class.java, responseTypeCaptor.firstValue)
    }
}
