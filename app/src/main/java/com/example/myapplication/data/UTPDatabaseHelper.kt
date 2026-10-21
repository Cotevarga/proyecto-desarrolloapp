package com.example.myapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val role: String, // "JEFATURA", "DOCENTE", "ALUMNO"
    val school: String,
    val status: String
)

data class Subject(
    val id: Long,
    val name: String,
    val unitsCount: Int,
    val level: String,
    val category: String
)

data class UnitObjective(
    val id: Long,
    val subjectId: Long,
    val title: String,
    val description: String,
    val isAuthorized: Boolean
)

data class ActivityItem(
    val id: Long,
    val title: String,
    val subject: String,
    val date: String,
    val time: String,
    val modality: String,
    val reminder: String
)

class UTPDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "utp_academic.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val TABLE_SUBJECTS = "subjects"
        const val TABLE_OBJECTIVES = "objectives"
        const val TABLE_ACTIVITIES = "activities"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users Table
        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL,
                school TEXT,
                status TEXT DEFAULT 'Activo'
            )
        """.trimIndent())

        // Create Subjects Table
        db.execSQL("""
            CREATE TABLE $TABLE_SUBJECTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                unitsCount INTEGER DEFAULT 0,
                level TEXT NOT NULL,
                category TEXT
            )
        """.trimIndent())

        // Create Objectives Table
        db.execSQL("""
            CREATE TABLE $TABLE_OBJECTIVES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                subjectId INTEGER,
                title TEXT NOT NULL,
                description TEXT,
                isAuthorized INTEGER DEFAULT 1
            )
        """.trimIndent())

        // Create Activities Table
        db.execSQL("""
            CREATE TABLE $TABLE_ACTIVITIES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                subject TEXT NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                modality TEXT NOT NULL,
                reminder TEXT
            )
        """.trimIndent())

        // Seed initial data
        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBJECTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OBJECTIVES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITIES")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        // Users
        db.execSQL("""
            INSERT INTO $TABLE_USERS (name, email, password, role, school, status) VALUES
            ('Maria Angelica', 'jefatura@utp.edu.pe', '123456', 'JEFATURA', 'Escuela Escritora Marcela Paz', 'Activo'),
            ('Profesor Carlos', 'carlos@utp.edu.pe', '123456', 'DOCENTE', 'Medicina Interna', 'Activo'),
            ('Profesora Alejandra', 'alejandra@utp.edu.pe', '123456', 'DOCENTE', 'Matemáticas', 'Activo'),
            ('Estudiante 01', 'estudiante@utp.edu.pe', '123456', 'ALUMNO', '1° Básico A', 'Activo')
        """.trimIndent())

        // Subjects
        db.execSQL("""
            INSERT INTO $TABLE_SUBJECTS (name, unitsCount, level, category) VALUES
            ('Matemáticas', 3, '1.° Básico', 'Números y operaciones'),
            ('Inglés', 2, '2.° Básico', 'Idiomas'),
            ('Lenguaje', 3, '1.° Básico', 'Comunicación'),
            ('Historia', 4, '1.° Básico', 'Ciencias Sociales')
        """.trimIndent())

        // Objectives
        db.execSQL("""
            INSERT INTO $TABLE_OBJECTIVES (subjectId, title, description, isAuthorized) VALUES
            (1, 'Contar y representar cantidades del 1 al 10', 'Identificación de cantidades mediante objetos e imágenes', 1),
            (3, 'Jugamos con los sonidos', 'Reconocer sonidos iniciales en palabras', 1),
            (4, 'Mi familia y mi comunidad', 'Reconocer integrantes y roles de la comunidad', 1)
        """.trimIndent())

        // Activities
        db.execSQL("""
            INSERT INTO $TABLE_ACTIVITIES (title, subject, date, time, modality, reminder) VALUES
            ('Revisión de caso clínico', 'Medicina Interna - Curso: 4° año', 'Mié 12 sep 2025', '08:30 - 10:00', 'Presencial - Sala 4B', '15 min antes'),
            ('Matemáticas Básica', '1A - 42 estudiantes', 'Hoy', '09:00 - 10:30', 'Sala 204', '10 min antes'),
            ('Álgebra Lineal', '1B - 38 estudiantes', 'Hoy', '11:00 - 12:30', 'Sala 307', '10 min antes')
        """.trimIndent())
    }

    fun getAllUsers(): List<User> {
        val list = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, email, role, school, status FROM $TABLE_USERS", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    User(
                        id = cursor.getLong(0),
                        name = cursor.getString(1),
                        email = cursor.getString(2),
                        role = cursor.getString(3),
                        school = cursor.getString(4) ?: "",
                        status = cursor.getString(5) ?: "Activo"
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun authenticateUser(email: String, pass: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, email, role, school, status FROM $TABLE_USERS WHERE email = ? AND password = ?", arrayOf(email, pass))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getLong(0),
                name = cursor.getString(1),
                email = cursor.getString(2),
                role = cursor.getString(3),
                school = cursor.getString(4) ?: "",
                status = cursor.getString(5) ?: "Activo"
            )
        }
        cursor.close()
        return user
    }

    fun getAllSubjects(): List<Subject> {
        val list = mutableListOf<Subject>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, unitsCount, level, category FROM $TABLE_SUBJECTS", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Subject(
                        id = cursor.getLong(0),
                        name = cursor.getString(1),
                        unitsCount = cursor.getInt(2),
                        level = cursor.getString(3),
                        category = cursor.getString(4) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllActivities(): List<ActivityItem> {
        val list = mutableListOf<ActivityItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, title, subject, date, time, modality, reminder FROM $TABLE_ACTIVITIES", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    ActivityItem(
                        id = cursor.getLong(0),
                        title = cursor.getString(1),
                        subject = cursor.getString(2),
                        date = cursor.getString(3),
                        time = cursor.getString(4),
                        modality = cursor.getString(5),
                        reminder = cursor.getString(6) ?: "15 min antes"
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
