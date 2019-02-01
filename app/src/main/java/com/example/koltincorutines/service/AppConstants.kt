package com.example.koltincorutines.service

import com.example.koltincorutines.BuildConfig

class AppConstants {

    companion object {
        const val TMDB_BASE_URL ="https://api.themoviedb.org/3/"

        var tmdbApiKey = BuildConfig.ApiKey
    }
}