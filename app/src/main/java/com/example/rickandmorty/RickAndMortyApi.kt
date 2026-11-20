package com.example.rickandmorty

import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacters(): CharacterResponse

    @GET("api/character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Character
}
