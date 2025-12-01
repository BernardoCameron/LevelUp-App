// MainViewModelTest.kt
package com.example.levelupapp.viewmodel

import com.example.levelupapp.MainDispatcherRule
import com.example.levelupapp.data.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun featuredProducts_returnsOnlyDestacados() = runTest {
        val vm = MainViewModel()

        val productos = listOf(
            Product(
                id = 1,
                nombre = "Destacado",
                descripcion = "",
                precio = 1000.0,
                imagen = "",
                categoriaId = 1,
                destacado = true,
                codigoBarras = null
            ),
            Product(
                id = 2,
                nombre = "Normal",
                descripcion = "",
                precio = 2000.0,
                imagen = "",
                categoriaId = 1,
                destacado = false,
                codigoBarras = null
            )
        )

        vm.setProductsForTest(productos)

        advanceUntilIdle() //deja que terminen los procesos de corutinas

        val featured = vm.featuredProducts.value

        assertEquals(1, featured.size)
        assertEquals(true, featured.first().destacado)
    }

    @Test
    fun recommendedFiltersByCategory_whenCategorySelected() = runTest {
        val vm = MainViewModel()

        val productos = listOf(
            Product(
                id = 1,
                nombre = "Cat1",
                descripcion = "",
                precio = 1000.0,
                imagen = "",
                categoriaId = 1,
                destacado = false,
                codigoBarras = null
            ),
            Product(
                id = 2,
                nombre = "Cat2",
                descripcion = "",
                precio = 2000.0,
                imagen = "",
                categoriaId = 2,
                destacado = false,
                codigoBarras = null
            )
        )

        vm.setProductsForTest(productos)
        vm.setCategoriaSeleccionada(1)

        advanceUntilIdle()

        val recommended = vm.recommendedProducts.value

        assertEquals(1, recommended.size)
        assertEquals(1, recommended.first().categoriaId)
    }
}
