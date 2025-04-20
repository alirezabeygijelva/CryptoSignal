package dev.alireza.fms.membership

import dev.alireza.fms.jooq.tables.records.RolesRecord
import dev.alireza.fms.jooq.tables.records.UsersRecord
import dev.alireza.fms.jooq.tables.references.RESET_PASSWORD_TOKENS
import dev.alireza.fms.jooq.tables.references.ROLES
import dev.alireza.fms.jooq.tables.references.USERS
import dev.alireza.fms.jooq.tables.references.USER_ROLES
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.types.ULong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Repository
class UserRepositoryImpl : UserRepository {

    @Autowired
    private lateinit var ctx: DSLContext

    override fun findById(id: Long): Optional<User> {
        val result = selectWithRoles()
            .where(USERS.ID.equal(id))
            .fetchGroups(
                { record -> record.into(USERS) },
                { record -> record.into(ROLES) }
            )

        return Optional.ofNullable(mapToUser(result))
    }

    override fun findByClientId(clientId: Long): Optional<User> {
        val result = selectWithRoles()
            .where(USERS.CLIENT_ID.equal(ULong.valueOf(clientId)))
            .fetchGroups(
                { record -> record.into(USERS) },
                { record -> record.into(ROLES) }
            )

        return Optional.ofNullable(mapToUser(result))
    }

    override fun findByEmail(email: String): Optional<User> {
        val result = selectWithRoles()
            .where(USERS.EMAIL.equal(email))
            .fetchGroups(
                { record -> record.into(USERS) },
                { record -> record.into(ROLES) }
            )

        return Optional.ofNullable(mapToUser(result))
    }

    override fun findByPhone(phone: String): Optional<User> {
        val result = selectWithRoles()
            .where(USERS.PHONE.equal(phone))
            .fetchGroups(
                { record -> record.into(USERS) },
                { record -> record.into(ROLES) }
            )

        return Optional.ofNullable(mapToUser(result))
    }

    override fun findByIdentifier(identifier: String): Optional<User> {
        val query = selectWithRoles()
        try {
            val result = query
                .where(USERS.CLIENT_ID.equal(ULong.valueOf(identifier)))
                .fetchGroups(
                    { record -> record.into(USERS) },
                    { record -> record.into(ROLES) }
                )
            return Optional.ofNullable(mapToUser(result))

        } catch (e: Exception) {
            val result = query
                .where(USERS.EMAIL.equal(identifier))
                .or(USERS.PHONE.equal(identifier))
                .fetchGroups(
                    { record -> record.into(USERS) },
                    { record -> record.into(ROLES) }
                )
            return Optional.ofNullable(mapToUser(result))
        }
    }

    override fun insertUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    ) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                val userId = dslContext.insertInto(
                    USERS,
                    USERS.FIRST_NAME,
                    USERS.LAST_NAME,
                    USERS.EMAIL,
                    USERS.PHONE,
                    USERS.PASSWORD,
                    USERS.ENABLED
                )
                    .values(firstName, lastName, email, phone, encodedPassword, 1)
                    .returningResult(USERS.ID)
                    .fetchOne()
                    ?.getValue(USERS.ID)

                dslContext.insertInto(USER_ROLES)
                    .set(
                        USER_ROLES.ROLE_ID,
                        dslContext.select(ROLES.ID).from(ROLES).where(ROLES.NAME.equal("ROLE_USER"))
                    )
                    .set(USER_ROLES.USER_ID, userId)
                    .execute()
            }
        }
    }

    override fun updateUser(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        telegramId: String?
    ) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(USERS)
                    .set(USERS.FIRST_NAME, firstName)
                    .set(USERS.LAST_NAME, lastName)
                    .set(USERS.EMAIL, email)
                    .set(USERS.PHONE, phone)
                    .set(USERS.TELEGRAM_ID, telegramId)
                    .where(USERS.ID.equal(id))
                    .execute()
            }
        }
    }

    override fun insertAdmin(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    ) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                val userId = dslContext.insertInto(
                    USERS,
                    USERS.FIRST_NAME,
                    USERS.LAST_NAME,
                    USERS.EMAIL,
                    USERS.PHONE,
                    USERS.PASSWORD,
                    USERS.ENABLED
                )
                    .values(firstName, lastName, email, phone, encodedPassword, 1)
                    .returningResult(USERS.ID)
                    .fetchOne()
                    ?.getValue(USERS.ID)

                dslContext.insertInto(USER_ROLES, USER_ROLES.ROLE_ID, USER_ROLES.USER_ID)
                    .values(getRoleId("ROLE_ADMIN"), userId)
                    .values(getRoleId("ROLE_USER"), userId)
                    .execute()
            }
        }
    }

    override fun insertRole(name: String, authorities: String) {
        val isRoleAlreadyExists = ctx.selectFrom(ROLES)
            .where(ROLES.NAME.equal(name))
            .fetch()
            .isNotEmpty
        if (!isRoleAlreadyExists) {
            ctx.transaction { configuration ->
                run {
                    val dslContext: DSLContext = DSL.using(configuration)
                    dslContext.insertInto(ROLES)
                        .set(ROLES.NAME, name)
                        .set(ROLES.AUTHORITIES, authorities)
                        .execute()
                }
            }
        }
    }

    override fun insertResetPasswordToken(resetToken: String, userId: Long, expiresAt: LocalDateTime) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.insertInto(RESET_PASSWORD_TOKENS)
                    .set(RESET_PASSWORD_TOKENS.RESET_TOKEN, resetToken)
                    .set(RESET_PASSWORD_TOKENS.USER_ID, userId)
                    .set(RESET_PASSWORD_TOKENS.EXPIRED_AT, expiresAt)
                    .execute()
            }
        }
    }

    override fun isResetPasswordTokenValid(resetToken: String): Boolean {
        val count = ctx.fetchCount(
            ctx.selectFrom(RESET_PASSWORD_TOKENS)
                .where(RESET_PASSWORD_TOKENS.RESET_TOKEN.eq(resetToken))
        )
        return count > 0
    }

    override fun findUserByResetPasswordToken(resetToken: String): Optional<User> {
        val userId = ctx.selectFrom(RESET_PASSWORD_TOKENS)
            .where(RESET_PASSWORD_TOKENS.RESET_TOKEN.eq(resetToken))
            .and(RESET_PASSWORD_TOKENS.EXPIRED_AT.greaterThan(LocalDateTime.now(ZoneOffset.UTC)))
            .and(RESET_PASSWORD_TOKENS.USED.equal(0))
            .fetchOne(RESET_PASSWORD_TOKENS.USER_ID)

        return if (userId != null) findById(userId) else Optional.empty()
    }

    override fun updatePassword(userId: Long, encodedPassword: String) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(USERS)
                    .set(USERS.PASSWORD, encodedPassword)
                    .where(USERS.ID.equal(userId))
                    .execute()
            }
        }
    }

    override fun useResetPasswordToken(resetToken: String) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(RESET_PASSWORD_TOKENS)
                    .set(RESET_PASSWORD_TOKENS.USED, 1)
                    .where(RESET_PASSWORD_TOKENS.RESET_TOKEN.eq(resetToken))
                    .and(RESET_PASSWORD_TOKENS.USED.equal(0))
                    .execute()
            }
        }
    }

    // -----------------------
    // Private Helper
    // -----------------------

    private fun getRoleId(roleName: String): Long? {
        return ctx.selectFrom(ROLES)
            .where(ROLES.NAME.equal(roleName))
            .fetchOne(ROLES.ID)
    }

    private fun selectWithRoles() = ctx.select(
        USERS.ID,
        USERS.CLIENT_ID,
        USERS.FIRST_NAME,
        USERS.LAST_NAME,
        USERS.EMAIL,
        USERS.PHONE,
        USERS.TELEGRAM_ID,
        USERS.PASSWORD,
        USERS.ENABLED,
        USERS.CREATED_AT,
        USERS.UPDATED_AT,
        ROLES.ID,
        ROLES.NAME,
        ROLES.AUTHORITIES
    )
        .from(USERS)
        .join(USER_ROLES).on(USERS.ID.equal(USER_ROLES.USER_ID))
        .join(ROLES).on(USER_ROLES.ROLE_ID.equal(ROLES.ID))

    private fun mapToUser(result: Map<UsersRecord, MutableList<RolesRecord>>) =
        result.map { (userRecord, roleRecords) ->
            User(
                id = userRecord.get(USERS.ID)!!,
                clientId = userRecord.get(USERS.CLIENT_ID)!!.toLong(),
                firstName = userRecord.get(USERS.FIRST_NAME)!!,
                lastName = userRecord.get(USERS.LAST_NAME)!!,
                email = userRecord.get(USERS.EMAIL)!!,
                phone = userRecord.get(USERS.PHONE)!!,
                telegramId = userRecord.get(USERS.TELEGRAM_ID),
                password = userRecord.get(USERS.PASSWORD)!!,
                enabled = userRecord.get(USERS.ENABLED)!! == 1.toByte(),
                roles = roleRecords.map { roleRecord ->
                    Role(
                        id = roleRecord.get(ROLES.ID)!!,
                        name = roleRecord.get(ROLES.NAME)!!,
                        authorities = roleRecord.get(ROLES.AUTHORITIES)!!.split(",")
                    )
                },
                createdAt = userRecord.get(USERS.CREATED_AT)!!.toInstant(ZoneOffset.UTC),
                updatedAt = userRecord.get(USERS.UPDATED_AT)!!.toInstant(ZoneOffset.UTC)
            )
        }.firstOrNull()
}