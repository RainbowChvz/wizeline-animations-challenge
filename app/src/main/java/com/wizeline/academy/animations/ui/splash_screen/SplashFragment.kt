package com.wizeline.academy.animations.ui.splash_screen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wizeline.academy.animations.databinding.SplashFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        animateLogo()
    }

    /**
    Animate the logo using an ObjectAnimator or ViewPropertyAnimator with at least 2 properties
    animated. Once the logo animation ends go to next fragment using a View.animation xml (set
    on the navigation graph)
     */
    private fun animateLogo() {

        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0F, 1F, 0.1F)
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 0.5F, 1.5F, 1F)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 0.5F, 1.5F, 1F)
        val rotate = PropertyValuesHolder.ofFloat(View.ROTATION, 0F, 360F)

        ObjectAnimator.ofPropertyValuesHolder(
            binding.ivWizelineLogo,
            alpha,
            scaleX,
            scaleY,
            rotate
        ).apply {
            duration = 3000
            interpolator = BounceInterpolator()
            doOnEnd {
                goToHomeScreen()
            }
            start()
        }
    }

    private fun goToHomeScreen() {
        val directions = SplashFragmentDirections.toMainFragment()
        findNavController().navigate(directions)
    }
}