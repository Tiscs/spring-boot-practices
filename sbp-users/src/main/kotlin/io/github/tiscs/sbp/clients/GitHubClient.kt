package io.github.tiscs.sbp.clients

import feign.RequestLine

interface GitHubClient {
    @RequestLine("GET /zen")
    fun fetchZen(): String
}
