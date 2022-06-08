package com.mytest.android.citiesapp.ui.quiz

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mytest.android.citiesapp.data.CapitalQuestion
import com.mytest.android.citiesapp.data.city.CityDatabase
import com.mytest.android.citiesapp.data.city.CityEntity
import com.mytest.android.citiesapp.data.city.CityRepository
import com.mytest.android.citiesapp.ui.quiz.QuizDifficulty.Companion.fromLevel
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val cityDao = CityDatabase.getDatabase(application).cityDao()
    private var repository = CityRepository(cityDao)

    private val _capitalQuestion: MutableLiveData<CapitalQuestion> = MutableLiveData()
    val capitalQuestions: LiveData<CapitalQuestion> get() = _capitalQuestion

    private val _timeToEnd: MutableLiveData<Long> = MutableLiveData(TIME_TO_END)
    val timeToEnd: LiveData<Long> get() = _timeToEnd

    private val _score: MutableLiveData<Int> = MutableLiveData(START_SCORE)
    val score: LiveData<Int> get() = _score

    private val _skipPoints: MutableLiveData<Int> = MutableLiveData(START_SKIP_POINTS)
    val skipPoints: LiveData<Int> get() = _skipPoints

    private val _isTimerEnd: MutableLiveData<Boolean> = MutableLiveData(false)
    val isTimerEnd: LiveData<Boolean> get() = _isTimerEnd

    private val _optionsList: MutableLiveData<MutableList<Int>> = MutableLiveData(mutableListOf(START_SCORE))
    val optionsList: LiveData<MutableList<Int>> get() = _optionsList

    private var listOfCountryAndCapital = mutableListOf<CityEntity>()

    private var listOfFourRandomCity = mutableListOf<String>()

    private var listOfFourRandomCityInRange = mutableListOf<String>()

    private var quizParameters: QuizDifficulty? = null

    private var countDownTimer: CountDownTimer? = null

    private var isListLoad = false

    fun fillListsWithCities() {
        if (!isListLoad) {
            isListLoad = true
            viewModelScope.launch {
                listOfCountryAndCapital = repository.findCountryWithCapital()
                when (quizParameters?.level) {
                    QuizDifficulty.MIDDLE.level -> listOfFourRandomCityInRange = repository.findFourRandomCityInRange()
                    QuizDifficulty.HARD.level -> listOfFourRandomCity = repository.findFourRandomCity()
                }
                makeQuestion()
            }

        }
    }

    fun makeQuestion() {
        when (quizParameters?.level) {
            QuizDifficulty.EASY.level -> makeEasyQuestion()
            QuizDifficulty.MIDDLE.level -> {
                buildQuestion(listOfCountryAndCapital, listOfFourRandomCityInRange)
            }
            QuizDifficulty.HARD.level -> {
                buildQuestion(listOfCountryAndCapital, listOfFourRandomCity)
            }
        }
    }

    private fun makeEasyQuestion() {
        if (listOfCountryAndCapital.isNotEmpty()) {
            listOfCountryAndCapital.shuffle()
            val optionList = mutableListOf<String>()
            optionList += listOfCountryAndCapital[ZERO_POSITION].city
            for (i in 1..3) {
                optionList += listOfCountryAndCapital[i].city
            }
            optionList.shuffle()
            _capitalQuestion.value = CapitalQuestion(
                listOfCountryAndCapital[ZERO_POSITION].country,
                listOfCountryAndCapital[ZERO_POSITION].city,
                optionList
            )
        }
    }

    private fun buildQuestion(listOfCapital: MutableList<CityEntity>, listOfCities: MutableList<String>) {
        if (listOfCapital.isNotEmpty() && listOfCities.isNotEmpty()) {
            listOfCapital.shuffle()
            listOfCities.shuffle()
            val optionList = mutableListOf<String>()
            optionList += listOfCapital[ZERO_POSITION].city
            for (i in 0..2) {
                if (listOfCapital[ZERO_POSITION].city != listOfCities[i])
                    optionList += listOfCities[i]
            }
            optionList.shuffle()
            _capitalQuestion.value = CapitalQuestion(
                listOfCapital[ZERO_POSITION].country,
                listOfCapital[ZERO_POSITION].city,
                optionList
            )
        }
    }

    fun startTimer() {
        val time = timeToEnd.value ?: TIME_TO_END
        countDownTimer = object : CountDownTimer(time, TIME_TICK) {
            override fun onTick(millisUntilFinished: Long) {
                _timeToEnd.value = millisUntilFinished
            }

            override fun onFinish() {
                cancel()
                _isTimerEnd.value = true
            }
        }.start()
    }

    fun endTimer() {
        countDownTimer?.cancel()
    }

    fun setQuizParameters(level: Int) {
        quizParameters = fromLevel(level)
    }

    fun decreaseSkipPoints() {
        _skipPoints.value = _skipPoints.value?.minus(ONE_SKIP_POINT)
    }

    fun decreaseTimeToEnd() {
        _timeToEnd.value = quizParameters?.penaltyTimeMs?.let { _timeToEnd.value?.minus(it) }
    }

    fun updateScore() {
        _score.value = _score.value?.plus(INCREASE_SCORE)
    }

    fun increaseTimeToEnd() {
        _timeToEnd.value = quizParameters?.let { _timeToEnd.value?.plus(it.bonusTimeMs) }
    }

    fun updateOptionsList(id: Int) {
        _optionsList.value?.add(id)
    }

    fun clearOptionsList() {
        _optionsList.value?.clear()
    }

    companion object {
        private const val ZERO_POSITION = 0
        private const val INCREASE_SCORE = 5
        private const val TIME_TICK = 100L
        private const val TIME_TO_END = 30000L
        private const val START_SKIP_POINTS = 3
        const val START_SCORE = 0
        private const val ONE_SKIP_POINT = 1
    }
}

enum class QuizDifficulty(val level: Int, val bonusTimeMs: Long, val penaltyTimeMs: Long) {
    EASY(1, 6000, 0),
    MIDDLE(2, 4000, 2000),
    HARD(3, 3000, 4000), ;

    companion object {
        fun fromLevel(level: Int): QuizDifficulty {
            return values().find { it.level == level } ?: EASY
        }
    }
}