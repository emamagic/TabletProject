package com.example.brightcity.api.safe

import java.io.IOException

class NoInternetException(message: String): IOException(message)
class NoToken(message: String): IOException(message)