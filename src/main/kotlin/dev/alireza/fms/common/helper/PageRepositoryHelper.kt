package dev.alireza.fms.common.helper

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.exception.ResponseException
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SortField
import org.jooq.Table
import org.jooq.kotlin.get
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus

object PageRepositoryHelper {

    fun <T : Record> findAll(ctx: DSLContext, table: Table<T>, pageable: Pageable): Page<T> {
        var orderFileds = emptyArray<SortField<out Any>?>()
        if (pageable.sort.isSorted) {
            orderFileds = pageable.sort
                .map { order ->
                    if (order.isAscending) table.get(order.property)?.asc() else table.get(order.property)?.desc()
                }
                .toList()
                .toTypedArray()
        }

        try {
            val content = ctx.selectFrom(table)
                .orderBy(*orderFileds)
                .limit(pageable.pageNumber, pageable.pageSize)
                .fetch()
                .toList()

            return PageImpl(
                content,
                pageable,
                ctx.fetchCount(table).toLong()
            )
        } catch (e: IllegalArgumentException) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.SORT_FIELD_NOT_SUPPORTED
            )
        }
    }

    fun <T : Record> findAll(
        ctx: DSLContext,
        table: Table<T>,
        pageable: Pageable,
        vararg condition: Condition
    ): Page<T> {
        var orderFileds = emptyArray<SortField<out Any>?>()
        if (pageable.sort.isSorted) {
            orderFileds = pageable.sort
                .map { order ->
                    if (order.isAscending) table.get(order.property)?.asc() else table.get(order.property)?.desc()
                }
                .toList()
                .toTypedArray()
        }

        try {
            val content = ctx.selectFrom(table)
                .where(*condition)
                .orderBy(*orderFileds)
                .limit(pageable.pageNumber, pageable.pageSize)
                .fetch()
                .toList()

            return PageImpl(
                content,
                pageable,
                ctx.fetchCount(ctx.selectFrom(table).where(*condition)).toLong()
            )
        } catch (e: IllegalArgumentException) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.SORT_FIELD_NOT_SUPPORTED
            )
        }
    }
}