package com.kenshi.livedatastateflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kenshi.livedatastateflow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        viewModel.liveDataCount.observe(this) { number ->
            binding.tvLivedata.text = number.toString()
        }

        // 해당 데이터 클래스의 변화를 관찰하여 반영한다
        viewModel.eventLiveData.observe(this) { event ->
            Log.d("eventLiveData", "${event.num1} ${event.num2}")
            binding.event1 = event
        }

        collectLatestLifecycleFlow(viewModel.eventStateFlow) { event ->
            Log.d("textFlow", "textFlow collect 호출")
            Log.d("textFlow", "${event.num1} ${event.num2}")
            binding.event2 = event
        }

        //두 개의 값이 특정 조건을 만족하면 imageView 가 보여지도록
        //특정 조건 두 카운터가 각각 5가 넘어가면 image show

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.stateFlowCount.collectLatest { number ->
//                    binding.tvFlow.text = number.toString()
//                }
//            }
//        }

        collectLatestLifecycleFlow(viewModel.stateFlowCount) { number ->
            Log.d("stateFlowCount", "stateFlowCount collect 호출")
            binding.tvFlow.text = number.toString()
        }

        binding.btnLivedata.setOnClickListener {
            viewModel.incrementLiveDataCounter()
        }

        binding.btnFlow.setOnClickListener {
            viewModel.incrementStateFlowCounter()
        }
    }

    // 계속 반복되는 함수이므로 재사용하기 위한 모듈화
    private fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}