package com.pune.dance.fitness.application.exceptions


class ContentNotFoundException : Exception {
    constructor() : super("Content not found")
    constructor(errorMessage: String) : super(errorMessage)
}