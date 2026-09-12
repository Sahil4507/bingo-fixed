package com.ishaan.bingo.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ishaan.bingo.data.remote.FirebaseGameDataSource
import com.ishaan.bingo.data.repository.BingoDatabase
import com.ishaan.bingo.data.repository.GameRepositoryImpl
import com.ishaan.bingo.data.repository.LocalGameRepository
import com.ishaan.bingo.data.repository.LocalPresetRepository
import com.ishaan.bingo.domain.model.BotDifficulty
import com.ishaan.bingo.ui.screens.game.GameViewModel
import com.ishaan.bingo.ui.screens.lobby.LobbyViewModel
import com.ishaan.bingo.ui.screens.result.ResultViewModel
import com.ishaan.bingo.ui.screens.settings.SettingsViewModel
import android.content.Context
import com.ishaan.bingo.data.repository.SettingsPreferences
import com.ishaan.bingo.ui.screens.settings.presets.PresetViewModel
import com.ishaan.bingo.ui.screens.setup.BoardSetupViewModel

object AppViewModelProvider {
    val repository = GameRepositoryImpl(FirebaseGameDataSource())
    lateinit var settingsViewModel: SettingsViewModel
    lateinit var presetRepository: LocalPresetRepository
    lateinit var settingsPreferences: SettingsPreferences

    var currentBotRepository: LocalGameRepository? = null

    fun freshBotRepository(difficulty: BotDifficulty = BotDifficulty.EASY): LocalGameRepository {
        val repo = LocalGameRepository(difficulty = difficulty)
        currentBotRepository = repo
        return repo
    }

    fun init(context: Context, db: BingoDatabase) {
        settingsPreferences = SettingsPreferences(context.applicationContext)
        settingsViewModel = SettingsViewModel(settingsPreferences)
        presetRepository = LocalPresetRepository(db)
    }

    // LobbyViewModel as a true singleton — one instance for the entire app lifetime
    val lobbyViewModel: LobbyViewModel by lazy { LobbyViewModel(repository) }

    // Factory for screens that still need viewModel() scoping (Settings, Presets)
    val Factory = viewModelFactory {
        initializer { settingsViewModel }
        initializer { PresetViewModel(presetRepository) }
    }

    fun boardSetupViewModelFactory(roomId: String, isBot: Boolean = false) = viewModelFactory {
        initializer {
            val repo = if (isBot) {
                val current = currentBotRepository
                if (current != null && (current.roomId == roomId || roomId.isEmpty())) {
                    current
                } else {
                    val newRepo = LocalGameRepository(roomId = if (roomId.isNotEmpty()) roomId else "bot-${java.util.UUID.randomUUID().toString().take(8)}")
                    currentBotRepository = newRepo
                    newRepo
                }
            } else {
                repository
            }
            BoardSetupViewModel(repo)
        }
    }

    fun gameViewModelFactory(roomId: String, isBot: Boolean = false) = viewModelFactory {
        initializer {
            val repo = if (isBot) {
                val current = currentBotRepository
                if (current != null && (current.roomId == roomId || roomId.isEmpty())) {
                    current
                } else {
                    val newRepo = LocalGameRepository(roomId = if (roomId.isNotEmpty()) roomId else "bot-${java.util.UUID.randomUUID().toString().take(8)}")
                    currentBotRepository = newRepo
                    newRepo
                }
            } else {
                repository
            }
            GameViewModel(repo, roomId)
        }
    }

    fun resultViewModelFactory(roomId: String, isBot: Boolean = false) = viewModelFactory {
        initializer {
            val repo = if (isBot) {
                val current = currentBotRepository
                if (current != null && (current.roomId == roomId || roomId.isEmpty())) {
                    current
                } else {
                    val newRepo = LocalGameRepository(roomId = if (roomId.isNotEmpty()) roomId else "bot-${java.util.UUID.randomUUID().toString().take(8)}")
                    currentBotRepository = newRepo
                    newRepo
                }
            } else {
                repository
            }
            ResultViewModel(repo, roomId)
        }
    }
}