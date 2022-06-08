package com.mytest.android.citiesapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.mytest.android.citiesapp.databinding.FragmentResultBinding
import com.mytest.android.citiesapp.utils.QuizPreferences.loadBestScore
import com.mytest.android.citiesapp.utils.QuizPreferences.saveBestScore
import com.mytest.android.citiesapp.ui.quiz.QuizDifficulty

class ResultFragment : NavigationCheckingFragment() {

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ResultFragmentArgs by navArgs()

        val bestScoreConstants = when (args.quizLevel) {
            QuizDifficulty.EASY.level -> BEST_EASY_LEVEL_SCORE
            QuizDifficulty.MIDDLE.level -> BEST_MIDDLE_LEVEL_SCORE
            QuizDifficulty.HARD.level -> BEST_HARD_LEVEL_SCORE
            else -> BEST_EASY_LEVEL_SCORE
        }
        val bestScore = loadBestScore(this.requireActivity(), bestScoreConstants)

        val score = args.score
        binding.currentScoreAmount.text = args.score.toString()

        if (bestScore < score) {
            binding.bestScoreAmount.text = args.score.toString()
            saveBestScore(this.requireActivity(), score, bestScoreConstants)
        } else {
            binding.bestScoreAmount.text = bestScore.toString()
        }

        binding.playAgainButton.setOnClickListener {
            navigate(ResultFragmentDirections.actionResultFragmentToChoiceOfDifficultyLevelFragment())
        }

        binding.backToMenuButton.setOnClickListener {
            navigate(ResultFragmentDirections.actionResultFragmentToMainFragment())
        }
    }

    companion object {
        private const val BEST_EASY_LEVEL_SCORE = "BEST_EASY_LVL_SCORE"
        private const val BEST_MIDDLE_LEVEL_SCORE = "BEST_MIDDLE_LVL_SCORE"
        private const val BEST_HARD_LEVEL_SCORE = "BEST_HARD_LVL_SCORE"
    }
}