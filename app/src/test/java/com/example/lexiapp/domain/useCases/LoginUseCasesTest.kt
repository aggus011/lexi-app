package com.example.lexiapp.domain.useCases

import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

class LoginUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: FirebaseAuth

    lateinit var loginUseCases: LoginUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginUseCases = LoginUseCases(mAuth)
    }
}