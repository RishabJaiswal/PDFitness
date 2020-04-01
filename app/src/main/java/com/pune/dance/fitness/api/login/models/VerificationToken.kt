package com.pune.dance.fitness.api.login.models

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthProvider

data class VerificationToken(
    var verificationID: String = "",
    var authCredential: AuthCredential? = null,
    var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
)