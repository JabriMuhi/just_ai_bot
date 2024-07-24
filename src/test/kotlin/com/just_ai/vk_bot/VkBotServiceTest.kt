package com.just_ai.vk_bot

import com.just_ai.vk_bot.services.VkBotService
import io.github.cdimascio.dotenv.Dotenv
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.ThreadLocalRandom

@ExtendWith(MockitoExtension::class)
class VkBotServiceTest {

    @Mock
    lateinit var restTemplate: RestTemplate

    private lateinit var dotenv: Dotenv
    private lateinit var vkBotService: VkBotService

    private val randomId = 12345

    @BeforeEach
    fun setUp() {
        dotenv = Dotenv.configure().load()
        vkBotService = VkBotService(restTemplate, dotenv)
    }

    @Test
    fun testSendMessage() {
        val userId = 123
        val message = "Test message"

        val mockRandom = mock(ThreadLocalRandom::class.java)
        mockStatic(ThreadLocalRandom::class.java).use { _ ->
            `when`(ThreadLocalRandom.current()).thenReturn(mockRandom)
            `when`(mockRandom.nextInt()).thenReturn(randomId)

            val accessToken = dotenv["VK_ACCESS_TOKEN"]
            val expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.vk.com/method/messages.send")
                .queryParam("user_id", userId)
                .queryParam("message", message)
                .queryParam("random_id", randomId)
                .queryParam("access_token", accessToken)
                .queryParam("v", "5.199")
                .build()
                .encode()
                .toUri()

            `when`(restTemplate.postForObject(eq(expectedUri), isNull(), eq(String::class.java))).thenReturn("Success")

            vkBotService.sendMessage(userId, message)

            verify(restTemplate, atLeastOnce()).postForObject(eq(expectedUri), isNull(), eq(String::class.java))
        }
    }

    @Test
    fun testRetryOnError() {
        val userId = 123
        val message = "Test message"

        val mockRandom = mock(ThreadLocalRandom::class.java)
        mockStatic(ThreadLocalRandom::class.java).use { _ ->
            `when`(ThreadLocalRandom.current()).thenReturn(mockRandom)
            `when`(mockRandom.nextInt()).thenReturn(randomId)

            val accessToken = dotenv["VK_ACCESS_TOKEN"]
            val expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.vk.com/method/messages.send")
                .queryParam("user_id", userId)
                .queryParam("message", message)
                .queryParam("random_id", randomId)
                .queryParam("access_token", accessToken)
                .queryParam("v", "5.199")
                .build()
                .encode()
                .toUri()

            `when`(restTemplate.postForObject(eq(expectedUri), isNull(), eq(String::class.java)))
                .thenThrow(RuntimeException("API call failed"))
                .thenReturn("Success")

            vkBotService.sendMessage(userId, message)

            verify(restTemplate, times(2)).postForObject(eq(expectedUri), isNull(), eq(String::class.java))
        }
    }

    @Test
    fun testUnauthorizedError() {
        val userId = 123
        val message = "Test message"

        val mockRandom = mock(ThreadLocalRandom::class.java)
        mockStatic(ThreadLocalRandom::class.java).use { _ ->
            `when`(ThreadLocalRandom.current()).thenReturn(mockRandom)
            `when`(mockRandom.nextInt()).thenReturn(randomId)

            val accessToken = dotenv["VK_ACCESS_TOKEN"]
            val expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.vk.com/method/messages.send")
                .queryParam("user_id", userId)
                .queryParam("message", message)
                .queryParam("random_id", randomId)
                .queryParam("access_token", accessToken)
                .queryParam("v", "5.199")
                .build()
                .encode()
                .toUri()

            `when`(restTemplate.postForObject(eq(expectedUri), isNull(), eq(String::class.java)))
                .thenThrow(HttpClientErrorException.Unauthorized::class.java)

            try {
                vkBotService.sendMessage(userId, message)
            } catch (e: HttpClientErrorException.Unauthorized) {
                //
            }

            verify(restTemplate, times(1)).postForObject(eq(expectedUri), isNull(), eq(String::class.java))
        }
    }

    @Test
    fun testServerError() {
        val userId = 123
        val message = "Test message"

        val mockRandom = mock(ThreadLocalRandom::class.java)
        mockStatic(ThreadLocalRandom::class.java).use { _ ->
            `when`(ThreadLocalRandom.current()).thenReturn(mockRandom)
            `when`(mockRandom.nextInt()).thenReturn(randomId)

            val accessToken = dotenv["VK_ACCESS_TOKEN"]
            val expectedUri = UriComponentsBuilder.fromHttpUrl("https://api.vk.com/method/messages.send")
                .queryParam("user_id", userId)
                .queryParam("message", message)
                .queryParam("random_id", randomId)
                .queryParam("access_token", accessToken)
                .queryParam("v", "5.199")
                .build()
                .encode()
                .toUri()

            `when`(restTemplate.postForObject(eq(expectedUri), isNull(), eq(String::class.java)))
                .thenThrow(HttpServerErrorException.InternalServerError::class.java)
                .thenThrow(HttpServerErrorException.InternalServerError::class.java)
                .thenReturn("Success")

            vkBotService.sendMessage(userId, message)

            verify(restTemplate, times(3)).postForObject(eq(expectedUri), isNull(), eq(String::class.java))
        }
    }
}
