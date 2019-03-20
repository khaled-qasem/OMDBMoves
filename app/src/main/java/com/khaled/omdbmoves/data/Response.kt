package com.khaled.omdbmoves.data

/**
 * A generic response class that holds a data and error with a [Status] to indicate if the response is successful
 */
data class Response<out DATA, out ERROR>(val status: Status, val data: DATA?, val error: ERROR?) {

    /**
     * map Response<DATA, ERROR> into Response<DATA2, ERROR>
     */
    fun <DATA2> map(map: (DATA?) -> DATA2?): Response<DATA2, ERROR> {
        return Response(status, map(data), error)
    }

    /**
     * map Response<DATA, ERROR> into Response<DATA2, ERROR>, pass a callback to map the data if it is not null.
     */
    fun <DATA2> mapNotNull(map: (DATA) -> DATA2?): Response<DATA2, ERROR> {
        return Response(status, data?.let { map(data) }, error)
    }

    companion object {
        fun <DATA, ERROR> success(data: DATA?): Response<DATA, ERROR> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <DATA, ERROR> error(error: ERROR?): Response<DATA, ERROR> {
            return Response(Status.ERROR, null, error)
        }
    }
}

enum class Status {
    SUCCESS, ERROR
}