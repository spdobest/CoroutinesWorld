package com.devtides.coroutinesroom.model

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: UserDatabase? = null
        private val LOCK = Any()
        private val DB_NAME = "UserDatabase"

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            DB_NAME
        ).build()


            private lateinit var instance1: UserDatabase
            fun getDbInstance(context: Context): UserDatabase {
                return if (!this::instance1.isInitialized) {
                    synchronized(UserDatabase::class) {
                        Room.databaseBuilder(
                            context.applicationContext,
                            UserDatabase::class.java,
                            DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .addMigrations(
                                *migrations
                            )
                            .build()
                    }
                } else {
                    instance1
                }

        }

    /**
     * For migration
     */
    @VisibleForTesting
    val migrations = arrayOf(
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN nickname TEXT DEFAULT 0 NOT NULL")
            }
        },
        object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_account_temporary` (`id` INTEGER NOT NULL, `state` INTEGER NOT NULL, `role` TEXT NOT NULL, `username` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("INSERT INTO user_account_temporary(id, state, role, username) SELECT id, state, role, nickname FROM user_account")
                database.execSQL("DROP TABLE user_account")
                database.execSQL("ALTER TABLE user_account_temporary RENAME TO user_account")
            }
        },
        object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_account_temporary` (`id` INTEGER NOT NULL, `state` INTEGER NOT NULL, `role` TEXT NOT NULL, `username` TEXT, PRIMARY KEY(`id`))")
                database.execSQL("INSERT INTO user_account_temporary(id, state, role, username) SELECT id, state, role, username FROM user_account")
                database.execSQL("DROP TABLE user_account")
                database.execSQL("ALTER TABLE user_account_temporary RENAME TO user_account")
            }
        },
        object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_account_temporary` (`id` INTEGER NOT NULL, `state` INTEGER NOT NULL, `role` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("INSERT INTO user_account_temporary(id, state, role) SELECT id, state, role FROM user_account")
                database.execSQL("DROP TABLE user_account")
                database.execSQL("ALTER TABLE user_account_temporary RENAME TO user_account")
            }
        },
        object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_account ADD COLUMN user_id INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("DROP INDEX IF EXISTS index_user_account_temporary_user_id")
                database.execSQL("CREATE TABLE IF NOT EXISTS `user_account_temporary` (`id` INTEGER NOT NULL, `state` INTEGER NOT NULL, `role` TEXT NOT NULL,`user_id` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_user_account_temporary_user_id ON user_account_temporary(user_id)")
                database.execSQL("INSERT INTO user_account_temporary(id, state, role, user_id) SELECT id, state, role, user_id FROM user_account")
                database.execSQL("DROP TABLE user_account")
                database.execSQL("ALTER TABLE user_account_temporary RENAME TO user_account")
            }
        }
    )
}


}