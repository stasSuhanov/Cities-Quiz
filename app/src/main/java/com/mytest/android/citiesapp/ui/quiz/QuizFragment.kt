package com.mytest.android.citiesapp.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mytest.android.citiesapp.R
import com.mytest.android.citiesapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment(), View.OnClickListener {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var binding: FragmentQuizBinding
    private val args: QuizFragmentArgs by navArgs()
    private var score: Int = QuizViewModel.START_SCORE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuiz()
        setOnClickListener()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        quizViewModel.endTimer()
    }

    private fun setupQuiz() {
        quizViewModel.setQuizParameters(args.quizLevel)
        quizViewModel.fillListsWithCities()
        quizViewModel.startTimer()
    }

    private fun setOnClickListener() {
        binding.firstOption.setOnClickListener(this)
        binding.secondOption.setOnClickListener(this)
        binding.thirdOption.setOnClickListener(this)
        binding.fourthOption.setOnClickListener(this)
        binding.skipButton.setOnClickListener(this)
    }

    private fun observeViewModel() {
        quizViewModel.capitalQuestions.observe(viewLifecycleOwner) { capitalQuestion ->
            with(binding) {
                question.text = getString(R.string.capital_question, capitalQuestion.country)
                firstOption.text = capitalQuestion.options[ZERO_OPTION_POSITION]
                secondOption.text = capitalQuestion.options[FIRST_OPTION_POSITION]
                thirdOption.text = capitalQuestion.options[SECOND_OPTION_POSITION]
                fourthOption.text = capitalQuestion.options[THIRD_OPTION_POSITION]
            }
        }

        quizViewModel.timeToEnd.observe(viewLifecycleOwner) {
            if (it < QuizViewModel.START_SCORE) {
                binding.timer.text = ZERO_POINTS.toString()
            } else {
                val minute = (it / TICK_TIME_FOR_CONVERT / SECONDS).toString().padStart(LENGTH_TIME, PAD_CHAR)
                val second = (it / TICK_TIME_FOR_CONVERT % SECONDS).toString().padStart(LENGTH_TIME, PAD_CHAR)
                binding.timer.text = getString(R.string.convert_time, minute, second)
            }
        }

        quizViewModel.score.observe(viewLifecycleOwner) {
            binding.currentScoreAmount.text = it.toString()
            score = it
        }

        quizViewModel.skipPoints.observe(viewLifecycleOwner) {
            binding.skipPointsAmount.text = it.toString()
            if (it == ZERO_POINTS) {
                binding.skipButton.visibility = View.INVISIBLE
            }
        }

        quizViewModel.isTimerEnd.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(QuizFragmentDirections.actionQuizFragmentToResultFragment(score, args.quizLevel))
            }
        }

        quizViewModel.optionsList.observe(viewLifecycleOwner) { optionList ->
            optionList.forEach {
                when (it) {
                    R.id.first_option -> {
                        binding.firstOption.setBackgroundResource(R.drawable.option_wrong)
                        binding.firstOption.isClickable = false
                    }
                    R.id.second_option -> {
                        binding.secondOption.setBackgroundResource(R.drawable.option_wrong)
                        binding.secondOption.isClickable = false
                    }
                    R.id.third_option -> {
                        binding.thirdOption.setBackgroundResource(R.drawable.option_wrong)
                        binding.thirdOption.isClickable = false
                    }
                    R.id.fourth_option -> {
                        binding.fourthOption.setBackgroundResource(R.drawable.option_wrong)
                        binding.fourthOption.isClickable = false
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.first_option, R.id.second_option, R.id.third_option, R.id.fourth_option -> {
                view.isClickable = false
                val selectedOption: TextView = view as TextView
                checkAnswer(selectedOption)
            }
            R.id.skip_button -> {
                quizViewModel.clearOptionsList()
                quizViewModel.decreaseSkipPoints()
                reset()
                quizViewModel.makeQuestion()
                fadeInAnimation()
            }
        }
    }

    private fun checkAnswer(option: TextView) {
        val selectedAnswer = option.text.toString()
        if (selectedAnswer == quizViewModel.capitalQuestions.value?.capital) {
            disableAllOptions()
            option.setBackgroundResource(R.drawable.option_right)
            quizViewModel.updateScore()
            fadeOutAnimation()
            quizViewModel.clearOptionsList()
            option.postDelayed({
                quizViewModel.increaseTimeToEnd()
                quizViewModel.endTimer()
                reset()
                fadeInAnimation()
                quizViewModel.makeQuestion()
                quizViewModel.startTimer()
            }, DELAY)
        } else {
            quizViewModel.updateOptionsList(option.id)
            option.setBackgroundResource(R.drawable.option_wrong)
            quizViewModel.endTimer()
            quizViewModel.decreaseTimeToEnd()
            quizViewModel.startTimer()
        }
    }

    private fun reset() = with(binding) {
        firstOption.setBackgroundResource(R.drawable.option_default)
        firstOption.isClickable = true
        secondOption.setBackgroundResource(R.drawable.option_default)
        secondOption.isClickable = true
        thirdOption.setBackgroundResource(R.drawable.option_default)
        thirdOption.isClickable = true
        fourthOption.setBackgroundResource(R.drawable.option_default)
        fourthOption.isClickable = true
    }

    private fun fadeInAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.faid_in_animation_300)
        with(binding) {
            firstOption.animation = fadeInAnimation
            secondOption.animation = fadeInAnimation
            thirdOption.animation = fadeInAnimation
            fourthOption.animation = fadeInAnimation
            question.animation = fadeInAnimation
        }
    }

    private fun fadeOutAnimation() {
        val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.faid_out_animation_200)
        with(binding) {
            firstOption.animation = fadeOutAnimation
            secondOption.animation = fadeOutAnimation
            thirdOption.animation = fadeOutAnimation
            fourthOption.animation = fadeOutAnimation
            question.animation = fadeOutAnimation
        }
    }

    private fun disableAllOptions() = with(binding) {
        firstOption.isClickable = false
        secondOption.isClickable = false
        thirdOption.isClickable = false
        fourthOption.isClickable = false
    }

    companion object {
        private const val ZERO_OPTION_POSITION = 0
        private const val FIRST_OPTION_POSITION = 1
        private const val SECOND_OPTION_POSITION = 2
        private const val THIRD_OPTION_POSITION = 3
        private const val ZERO_POINTS = 0
        private const val TICK_TIME_FOR_CONVERT = 1000
        private const val SECONDS = 60
        private const val DELAY = 100L
        private const val LENGTH_TIME = 2
        private const val PAD_CHAR = '0'
    }
}