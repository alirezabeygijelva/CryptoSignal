package dev.alireza.fms.common.constant

import dev.alireza.fms.common.exception.ApiErrorKind

object ApiErrorConstant {

    val SORT_FIELD_NOT_SUPPORTED = ApiErrorKind("Sort field not supported", "SORT_FIELD_NOT_SUPPORTED")
    val INVALID_MARKET_TYPE = ApiErrorKind("Market type is invalid", "INVALID_MARKET_TYPE")
    val MARKET_EXISTS = ApiErrorKind("Market already exists", "MARKET_EXISTS")
    val MARKET_NOT_FOUND = ApiErrorKind("No market was found", "MARKET_NOT_FOUND")
    val BINANCE_STREAM_EXISTS = ApiErrorKind("Binance Stream already exists", "BINANCE_EXISTS")
    val BINANCE_STREAM_NOT_FOUND = ApiErrorKind("Binance Stream does not exist", "BINANCE_STREAM_NOT_FOUND")
    val ASSETS_NOT_FOUND = ApiErrorKind("Assets not found", "ASSETS_NOT_FOUND")
    val SYMBOL_NOT_FOUND = ApiErrorKind("Symbol not found", "SYMBOL_NOT_FOUND")
    val SYMBOL_NOT_MATCH = ApiErrorKind("The symbol does not match the market id", "SYMBOL_NOT_MATCH")
    val INVALID_TARGET_TYPE = ApiErrorKind("Target type is invalid", "INVALID_TARGET_TYPE")
    val SUBSCRIPTION_EXISTS = ApiErrorKind("Subscription already exists", "SUBSCRIPTION_EXISTS")
    val SUBSCRIPTION_NOT_FOUND = ApiErrorKind("Subscription not found", "SUBSCRIPTION_NOT_FOUND")
    val USER_NOT_FOUND = ApiErrorKind("User not found", "USER_NOT_FOUND")
    val INVALID_USERNAME = ApiErrorKind("The input information does not match any user", "INVALID_USERNAME")
    val UNAVAILABLE_EMAIL = ApiErrorKind("Email is not available", "UNAVAILABLE_EMAIL")
    val UNAVAILABLE_PHONE = ApiErrorKind("Phone is not available", "UNAVAILABLE_PHONE")
    val INVALID_USERNAME_OR_PASSWORD = ApiErrorKind("Username or password is invalid", "INVALID_USERNAME_OR_PASSWORD")
    val INVALID_PASSWORD = ApiErrorKind("The password is incorrect", "INVALID_PASSWORD")
    val INVALID_REFRESH_TOKEN = ApiErrorKind("Refresh token is invalid", "INVALID_REFRESH_TOKEN")
    val PASSWORD_NOT_MATCH = ApiErrorKind("Password and confirmation do not match", "PASSWORD_NOT_MATCH")
    val INVALID_RESET_PASSWORD_TOKEN = ApiErrorKind("Reset Password Token does not exist or has expired", "INVALID_RESET_PASSWORD_TOKEN")
}