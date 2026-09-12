package mx.tec.sabores.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.sabores.data.RestaurantRepository
import mx.tec.sabores.domain.RatingSummary
import mx.tec.sabores.domain.Restaurant
import mx.tec.sabores.domain.RestaurantEnLista
import mx.tec.sabores.domain.Review
import retrofit2.HttpException
import java.io.IOException

data class MyReviewItem(val restaurantName: String, val review: Review)

/** El restaurante y sus reseñas, que la pantalla de detalle necesita juntos. */
data class Detalle(
    val restaurant: Restaurant,
    val reviews: List<Review>
) {
    // La regla del dominio sigue viva: el promedio se calcula aquí, no se hereda
    // del servidor, para que cambie al instante al publicar tu reseña.
    val summary: RatingSummary = RatingSummary.from(reviews)
}

class SaboresViewModel(
    private val repository: RestaurantRepository = RestaurantRepository()
) : ViewModel() {

    // Ya no se lee una vez al construir: ahora llega de la red, y tarda.
    var restaurantes by mutableStateOf<UiState<List<RestaurantEnLista>>>(UiState.Cargando)
        private set

    var detalle by mutableStateOf<UiState<Detalle>>(UiState.Cargando)
        private set

    var mias by mutableStateOf<UiState<List<MyReviewItem>>>(UiState.Cargando)
        private set

    init {
        cargarRestaurantes()
        cargarMisResenas()
    }

    fun cargarRestaurantes() {
        viewModelScope.launch {
            restaurantes = UiState.Cargando
            restaurantes = pedir { repository.getAllForList() }
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            detalle = UiState.Cargando
            detalle = pedir { Detalle(repository.getById(id), repository.getReviews(id)) }
        }
    }

    fun cargarMisResenas() {
        viewModelScope.launch {
            mias = UiState.Cargando
            mias = pedir {
                repository.getMyReviews().mapNotNull { review ->
                    repository.getById(review.restaurantId).let { MyReviewItem(it.name, review) }
                }
            }
        }
    }

    fun borrarResena(id: Int) {
        viewModelScope.launch {
            try {
                if (repository.deleteReview(id)) {
                    cargarMisResenas() // Recargamos la lista
                }
            } catch (e: IOException) {
                // Manejar error de red si es necesario (ej. Toast o Snackbar)
            } catch (e: HttpException) {
                // Manejar error de API (ej. 403)
            }
        }
    }

    private suspend fun <T> pedir(block: suspend () -> T): UiState<T> = try {
        UiState.Exito(block())
    } catch (e: IOException) {
        UiState.Error("No hay conexión. Revisa tu internet.")
    } catch (e: HttpException) {
        UiState.Error(mensajeDe(e))
    }

    // --- eventos que llegan desde la UI ---

    fun addReview(restaurantId: Int, stars: Int, comment: String) {
        // Todavía no guarda: publicar contra el servidor es el Bloque C.
    }
}
