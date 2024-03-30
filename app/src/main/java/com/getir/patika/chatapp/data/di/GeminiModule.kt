package com.getir.patika.chatapp.data.di

import android.content.Context
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.impl.GeminiDataSource
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import com.getir.patika.chatapp.R.string as AppText

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {
    private val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
    private val hateSpeechSafety =
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)

    private val config = generationConfig {
        temperature = 0.8f
        topK = 16
        topP = 0.1f
        maxOutputTokens = 300
        stopSequences = listOf("red")
    }

    @Singleton
    @Provides
    fun provideGenerativeModelForText(@ApplicationContext context: Context): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = context.getString(AppText.gemini_api_key),
            safetySettings = listOf(harassmentSafety, hateSpeechSafety),
            generationConfig = config
        )

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun bindGeminiService(
        generativeModel: GenerativeModel,
        ioDispatcher: CoroutineDispatcher
    ): GeminiRepository =
        GeminiDataSource(generativeModel, ioDispatcher)
}
