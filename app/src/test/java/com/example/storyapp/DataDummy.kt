package com.example.storyapp

import com.example.storyapp.data.response.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "id + $i",
                "name + $i",
                "description + $i",
                i.toString(),
                "created + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}