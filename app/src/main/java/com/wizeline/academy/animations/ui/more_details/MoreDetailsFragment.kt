package com.wizeline.academy.animations.ui.more_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.annotation.FloatRange
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.wizeline.academy.animations.databinding.MoreDetailsFragmentBinding
import com.wizeline.academy.animations.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreDetailsFragment : Fragment() {

    private var _binding: MoreDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MoreDetailsViewModel

    private val args: MoreDetailsFragmentArgs by navArgs()

    private lateinit var scaleXAnim: SpringAnimation
    private lateinit var scaleYAnim: SpringAnimation
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreDetailsFragmentBinding.inflate(inflater, container, false)
        binding.ivImageDetailLarge.loadImage(args.imageId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) { binding.tvTitle.text = it }
        viewModel.content.observe(viewLifecycleOwner) { binding.tvFullTextContent.text = it }
        viewModel.fetchData(args.contentIndex)

        setupZoomAnimation()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupZoomAnimation() {
        scaleXAnim = createSpringAnimation(
            binding.ivImageDetailLarge,
            SpringAnimation.SCALE_X,
        )

        scaleYAnim = createSpringAnimation(
            binding.ivImageDetailLarge,
            SpringAnimation.SCALE_Y,
        )

        setUpPinchToZoom()

        binding.ivImageDetailLarge.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                scaleXAnim.start()
                scaleYAnim.start()
            } else {
                scaleXAnim.cancel()
                scaleYAnim.cancel()

                scaleGestureDetector.onTouchEvent(motionEvent)
            }

            true
        }

    }

    private fun createSpringAnimation(
        view: View,
        property: DynamicAnimation.ViewProperty,
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(1.0F)
        spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
        spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
        animation.spring = spring

        return animation
    }

    private fun setUpPinchToZoom() {
        var scaleFactor = 1f
        scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object: ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector?): Boolean {
                    scaleFactor *= detector?.scaleFactor ?: 1.0f
                    binding.ivImageDetailLarge.scaleX *= scaleFactor
                    binding.ivImageDetailLarge.scaleY *= scaleFactor

                    return true
                }
            }
        )
    }
}