package com.khaled.omdbmoves

import com.khaled.omdbmoves.data.database.MoviesDao
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.data.model.MoviesApiResponse
import com.khaled.omdbmoves.data.network.MoviesApiServices
import com.khaled.omdbmoves.repository.MoviesRepository
import com.khaled.omdbmoves.repository.MoviesRepositoryImpl
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class MoviesRepositoryTest {

    private val movie1 = createMovieWithId(1)
    private val movie2 = createMovieWithId(2)
    private val movies = listOf(movie1, movie2)

    private  val moviesApiServices: MoviesApiServices = mock()
    private val moviesDao: MoviesDao= mock()

    private val moviesRepository: MoviesRepository =
        MoviesRepositoryImpl(moviesApiServices, moviesDao)


    @Test
    fun testAddition() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getMoviesFromNetworkTest() = runBlocking {
        whenever(moviesApiServices.getTopRatedMovies(
                "",
                "",
                0,
                "",
                false,
                false,
                0
            )
        ).thenReturn(MoviesApiResponse(1, movies, 3, 100))

        val response =  moviesApiServices.getTopRatedMovies(
            "",
            "",
            0,
            "",
            false,
            false,
            0
        )
        verify(moviesApiServices).getTopRatedMovies(
            "",
            "",
            0,
            "",
            false,
            false,
            0
        )

        assertEquals(response.movies,movies)
    }

}



fun createMovieWithId(id: Long) =
    Movie(
        id, null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )