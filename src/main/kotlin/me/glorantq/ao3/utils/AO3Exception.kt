package me.glorantq.ao3.utils

/**
 * The exception this API throws.
 *
 * @author Gerber Lóránt Viktor
 * @since 1.0
 */
class AO3Exception : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex)
    constructor(message: String): super(message)
    constructor(ex: Exception): super(ex)
}