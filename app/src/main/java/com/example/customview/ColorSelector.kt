package com.example.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.customview.databinding.ColorSelectorBinding

//Java haven't concept of default parameters so if you wanna this component work in java
//you should add annotation @JvmOverloads this annotations will generate four different constructors
class ColorSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defRes: Int = 0
): LinearLayout(context, attrs, defStyle, defRes) {
    private var listOfColors= listOf(Color.BLACK,Color.BLUE,Color.RED)
    private var selectedColorIndex=0
    private var binding: ColorSelectorBinding

    companion object{
        var onColorSelected:((color:Int)->Unit)?=null
    }

    init {
        binding=ColorSelectorBinding.inflate(LayoutInflater.from(context),this)

        //get array of colors from style file that takes list of colors from string file
        val typedArray=context.obtainStyledAttributes(attrs,R.styleable.ColorSelector)
        listOfColors=typedArray.getTextArray(R.styleable.ColorSelector_colors).map { Color.parseColor(it.toString())}
        typedArray.recycle() // To free up memory and resource when you're done

        orientation = HORIZONTAL
        gravity=Gravity.CENTER_HORIZONTAL
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        binding.leftArrow.setOnClickListener(OnClickListener {
            selectPreviousColor()
        })

        binding.rightArrow.setOnClickListener(OnClickListener {
            selectNextColor()
        })
    }

    private fun selectNextColor() {
        if(selectedColorIndex==listOfColors.lastIndex)selectedColorIndex=0
        else selectedColorIndex++
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun selectPreviousColor() {
        if(selectedColorIndex==0)selectedColorIndex=listOfColors.lastIndex
        else selectedColorIndex--
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun broadcastColor(){
        if(binding.colorEnabled.isChecked) onColorSelected?.invoke(listOfColors[selectedColorIndex])
        else onColorSelected?.invoke(Color.TRANSPARENT)
    }
}