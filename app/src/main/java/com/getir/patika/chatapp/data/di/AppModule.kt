package com.getir.patika.chatapp.data.di

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.getir.patika.chatapp.R
import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.data.GeminiRepository
import com.getir.patika.chatapp.data.PreferencesRepository
import com.getir.patika.chatapp.data.impl.ChatDataSource
import com.getir.patika.chatapp.data.impl.GeminiDataSource
import com.getir.patika.chatapp.data.impl.PreferencesDataSource
import com.getir.patika.chatapp.data.room.ChatDao
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
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import com.getir.patika.chatapp.R.string as AppText

/**
 * Dagger module providing dependencies related to data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)
    private val hateSpeechSafety =
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)

    private val config = generationConfig {
        temperature = 0.8f
        topK = 16
        topP = 0.1f
        maxOutputTokens = 1500
    }

    /**
     * Provides a singleton instance of [GenerativeModel] for generating text messages.
     */
    @Singleton
    @Provides
    fun provideGenerativeModelForText(@ApplicationContext context: Context): GenerativeModel =
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = context.getString(AppText.gemini_api_key),
            safetySettings = listOf(harassmentSafety, hateSpeechSafety),
            generationConfig = config
        )

    /**
     * Provides the IO dispatcher for performing disk IO operations.
     */
    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides a singleton instance of [DataStore] for storing user preferences.
     */
    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideMarkwon(@ApplicationContext context: Context): Markwon {
        val plugin = object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                val resources = context.resources
                builder
                    .linkColor(ContextCompat.getColor(context, R.color.markdown_link))
                    .blockQuoteColor(ContextCompat.getColor(context, R.color.markdown_blockquote))
                    .bulletWidth(resources.getDimensionPixelSize(R.dimen.markdown_bullet_Width))
                    .codeBlockTextColor(
                        ContextCompat.getColor(context, R.color.markdown_code_block_text)
                    )
                    .codeBackgroundColor(
                        ContextCompat.getColor(context, R.color.markdown_code_block_bg)
                    )
                    .isLinkUnderlined(false)
            }
        }
        return Markwon
            .builder(context)
            .usePlugin(plugin)
            .build()
    }

    /**
     * Binds [GeminiRepository] implementation.
     */
    @Singleton
    @Provides
    fun bindGeminiRepository(
        generativeModel: GenerativeModel,
        ioDispatcher: CoroutineDispatcher
    ): GeminiRepository =
        GeminiDataSource(generativeModel, ioDispatcher)

    /**
     * Binds [PreferencesRepository] implementation.
     */
    @Singleton
    @Provides
    fun bindPreferencesRepository(
        userPreferences: DataStore<Preferences>
    ): PreferencesRepository = PreferencesDataSource(userPreferences)

    /**
     * Binds [ChatRepository] implementation.
     */
    @Singleton
    @Provides
    fun bindChatRepository(
        chatDao: ChatDao,
        geminiRepository: GeminiRepository,
        preferencesRepository: PreferencesRepository,
        ioDispatcher: CoroutineDispatcher,
    ): ChatRepository =
        ChatDataSource(chatDao, geminiRepository, preferencesRepository, ioDispatcher)

    private const val USER_PREFERENCES_NAME = "user_preferences"
}
