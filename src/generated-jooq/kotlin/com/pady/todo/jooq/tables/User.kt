/*
 * This file is generated by jOOQ.
 */
package com.pady.todo.jooq.tables


import com.pady.todo.jooq.DefaultSchema
import com.pady.todo.jooq.keys.USER_U_ID_PK
import com.pady.todo.jooq.tables.records.UserRecord

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row3
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class User(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, UserRecord>?,
    aliased: Table<UserRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<UserRecord>(
    alias,
    DefaultSchema.DEFAULT_SCHEMA,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>USER</code>
         */
        val USER: User = User()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<UserRecord> = UserRecord::class.java

    /**
     * The column <code>USER.U_ID</code>.
     */
    val U_ID: TableField<UserRecord, Long?> = createField(DSL.name("U_ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>USER.NAME</code>.
     */
    val NAME: TableField<UserRecord, String?> = createField(DSL.name("NAME"), SQLDataType.VARCHAR(2147483647).nullable(false), this, "")

    /**
     * The column <code>USER.SURNAME</code>.
     */
    val SURNAME: TableField<UserRecord, String?> = createField(DSL.name("SURNAME"), SQLDataType.VARCHAR(2147483647).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<UserRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<UserRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>USER</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>USER</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>USER</code> table reference
     */
    constructor(): this(DSL.name("USER"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, UserRecord>): this(Internal.createPathAlias(child, key), child, key, USER, null)
    override fun getSchema(): Schema? = if (aliased()) null else DefaultSchema.DEFAULT_SCHEMA
    override fun getIdentity(): Identity<UserRecord, Long?> = super.getIdentity() as Identity<UserRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<UserRecord> = USER_U_ID_PK
    override fun `as`(alias: String): User = User(DSL.name(alias), this)
    override fun `as`(alias: Name): User = User(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): User = User(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): User = User(name, null)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<Long?, String?, String?> = super.fieldsRow() as Row3<Long?, String?, String?>
}
