package com.mytest.android.citiesapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentChoiceOfDifficultyLevelBinding
import com.mytest.android.citiesapp.ui.quiz.QuizDifficulty

class ChoiceOfDifficultyLevelFragment : NavigationCheckingFragment(), View.OnClickListener {

    private lateinit var binding: FragmentChoiceOfDifficultyLevelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChoiceOfDifficultyLevelBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.easyLvlButton.setOnClickListener(this)
        binding.middleLvlButton.setOnClickListener(this)
        binding.hardLvlButton.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val quizLevel: Int = when (view.id) {
            R.id.easy_lvl_button -> QuizDifficulty.EASY.level
            R.id.middle_lvl_button -> QuizDifficulty.MIDDLE.level
            R.id.hard_lvl_button -> QuizDifficulty.HARD.level
            else -> QuizDifficulty.EASY.level
        }

        navigate(ChoiceOfDifficultyLevelFragmentDirections.actionChoiceOfDifficultyLevelFragmentToQuizFragment(quizLevel))
    }
}