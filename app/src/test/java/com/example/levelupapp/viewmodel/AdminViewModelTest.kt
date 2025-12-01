package com.example.levelupapp.viewmodel

import com.example.levelupapp.MainDispatcherRule
import com.example.levelupapp.data.model.Product
import com.example.levelupapp.fakes.FakeProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun deleteProduct_removesProductFromList() = runTest {
        val initial = listOf(
            Product(
                id = 1,
                nombre = "Prod1",
                descripcion = "",
                precio = 1000.0,
                imagen = "",
                categoriaId = 1,
                destacado = false,
                codigoBarras = null
            ),
            Product(
                id = 2,
                nombre = "Prod2",
                descripcion = "",
                precio = 2000.0,
                imagen = "",
                categoriaId = 1,
                destacado = false,
                codigoBarras = null
            )
        )

        val fakeRepo = FakeProductRepository(initialProducts = initial)
        val vm = AdminViewModel(repository = fakeRepo)

        advanceUntilIdle()
        Assert.assertEquals(2, vm.products.value.size)

        vm.deleteProduct(1)
        advanceUntilIdle()

        Assert.assertEquals(1, vm.products.value.size)
        Assert.assertEquals(2, vm.products.value.first().id)
    }

    @Test
    fun addProduct_addsProductToList() = runTest {
        val fakeRepo = FakeProductRepository()
        val vm = AdminViewModel(repository = fakeRepo)

        advanceUntilIdle()
        Assert.assertEquals(0, vm.products.value.size)

        val newProduct = Product(
            id = 0,
            nombre = "Nuevo",
            descripcion = "",
            precio = 5000.0,
            imagen = "",
            categoriaId = 1,
            destacado = false,
            codigoBarras = null
        )

        vm.addProduct(newProduct)
        advanceUntilIdle()

        Assert.assertEquals(1, vm.products.value.size)
        Assert.assertEquals("Nuevo", vm.products.value.first().nombre)
    }
}