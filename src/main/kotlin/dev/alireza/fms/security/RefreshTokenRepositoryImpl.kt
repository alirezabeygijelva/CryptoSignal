package dev.alireza.fms.security

import dev.alireza.fms.jooq.tables.references.REFRESH_TOKENS
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Repository
class RefreshTokenRepositoryImpl : RefreshTokenRepository {

    @Autowired
    private lateinit var ctx: DSLContext

    override fun hasAnyUnexpiredToken(userId: Long, token: String): Boolean {
        return ctx.selectFrom(REFRESH_TOKENS)
            .where(REFRESH_TOKENS.TOKEN.equal(token))
            .and(REFRESH_TOKENS.USER_ID.equal(userId))
            .and(REFRESH_TOKENS.EXPIRED_AT.greaterThan(LocalDateTime.now(ZoneOffset.UTC)))
            .fetch().isNotEmpty
    }

    override fun insertNewRefreshToken(userId: Long, expiredAt: LocalDateTime): String {
        val token: String = UUID.randomUUID().toString()
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.insertInto(REFRESH_TOKENS)
                    .set(REFRESH_TOKENS.USER_ID, userId)
                    .set(REFRESH_TOKENS.TOKEN, token)
                    .set(REFRESH_TOKENS.EXPIRED_AT, expiredAt)
                    .set(REFRESH_TOKENS.CREATED_AT, LocalDateTime.now(ZoneOffset.UTC))
                    .set(REFRESH_TOKENS.UPDATED_AT, LocalDateTime.now(ZoneOffset.UTC))
                    .execute()
            }
        }
        return token
    }
}