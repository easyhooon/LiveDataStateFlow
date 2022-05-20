package com.kenshi.livedatastateflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {

    private val _liveDataCount = MutableLiveData<Int>()
    val liveDataCount: LiveData<Int> = _liveDataCount

    private val _stateFlowCount = MutableStateFlow(0)
    val stateFlowCount: StateFlow<Int> = _stateFlowCount

    private val _eventLiveData = MutableLiveData<Event>()
    val eventLiveData:LiveData<Event> = _eventLiveData

    private val _eventStateFlow = MutableStateFlow(Event(5, 0))
    val eventStateFlow:StateFlow<Event> = _eventStateFlow

    init {
        _liveDataCount.value = 0
        _eventLiveData.value = Event(5, 0)
    }

    fun incrementLiveDataCounter() {
        _liveDataCount.value = _liveDataCount.value?.plus(1)
        //변경되는 것을 확인
        //_eventLiveData.value = Event(10, 10)

        //변화한 값이 반영되지 않음
        //_eventLiveData.value!!.num2 = _eventLiveData.value!!.num1.plus(1)
        _eventLiveData.value =_eventLiveData.value?.copy(num2 = _eventLiveData.value!!.num2.plus(1))
    }

    //값을 copy 한 다음에 그 객체를 통째로 setValue
    fun incrementStateFlowCounter() {
        _stateFlowCount.value += 1
        //변경 되는 것을 확인
        //_eventStateFlow.value = Event(10, 10)

        //변화한 값이 반영되지 않음
        //_eventStateFlow.value.num2 += 1

        _eventStateFlow.value = _eventStateFlow.value.copy(num2 = _eventStateFlow.value.num2 + 1)
    }
}