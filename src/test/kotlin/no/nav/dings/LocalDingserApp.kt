package no.nav.dings

import io.ktor.util.KtorExperimentalAPI
import no.nav.dings.config.Environment
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.OAuth2Config

@KtorExperimentalAPI
fun main() {
    val mockOAuth2Server = MockOAuth2Server(OAuth2Config(interactiveLogin = false))
    mockOAuth2Server.start()
    createHttpServer(
        Environment(
            Environment.Application(
                port = 8282
            ),
            Environment.Login(),
            Environment.Idporten(
                wellKnownUrl = mockOAuth2Server.wellKnownUrl("idporten").toString(),
                privateJwk = generateRsaKey().first.toJSONObject().toJSONString(),
                clientId = "101010"
            ),
            Environment.TokenX(
                wellKnownUrl = mockOAuth2Server.wellKnownUrl("tokenx").toString(),
                privateJwk = generateRsaKey().first.toJSONObject().toJSONString(),
                clientId = "909090"
            )
        ),
        ApplicationStatus()
    ).start(wait = true)
}
