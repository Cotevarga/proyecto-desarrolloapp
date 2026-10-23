package com.example.myapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Clases de modelo para mapeo de datos
data class TaskModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val badge: String
)

data class ClassModel(
    val id: Int,
    val time: String,
    val name: String,
    val students: String,
    val room: String
)

class UTPDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "utp_database.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Tabla de Usuarios para Login
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                password TEXT,
                nombre TEXT,
                rol TEXT
            )
        """.trimIndent())

        // 2. Tabla de Tareas (Pantalla Alumno)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                subtitle TEXT,
                badge TEXT
            )
        """.trimIndent())

        // 3. Tabla de Clases (Pantalla Docente)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS teacher_classes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                time TEXT,
                name TEXT,
                students TEXT,
                room TEXT
            )
        """.trimIndent())

        // Insertar registros de prueba iniciales
        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS tasks")
        db.execSQL("DROP TABLE IF EXISTS teacher_classes")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Cuentas para el Login
        db.execSQL("INSERT OR IGNORE INTO usuarios (email, password, nombre, rol) VALUES ('alumno@utp.edu', '123456', 'Alberto Gómez', 'ALUMNO')")
        db.execSQL("INSERT OR IGNORE INTO usuarios (email, password, nombre, rol) VALUES ('docente@utp.edu', '123456', 'María Angélica', 'DOCENTE')")
        db.execSQL("INSERT OR IGNORE INTO usuarios (email, password, nombre, rol) VALUES ('jefatura@utp.edu', '123456', 'Carlos Mendoza', 'JEFATURA')")

        // Tareas del estudiante
        db.execSQL("INSERT INTO tasks (title, subtitle, badge) VALUES ('Ejercicios de derivadas', 'Matemáticas • Entrega hoy', 'Hoy')")
        db.execSQL("INSERT INTO tasks (title, subtitle, badge) VALUES ('Resumen de reacciones', 'Química • Entrega mañana', 'Mañana')")
        db.execSQL("INSERT INTO tasks (title, subtitle, badge) VALUES ('Lectura de Cien años', 'Literatura • Capítulo 3', 'Semana')")

        // Clases del docente
        db.execSQL("INSERT INTO teacher_classes (time, name, students, room) VALUES ('09:00 - 10:30', 'Matemáticas Básica', '1A • 42 estudiantes', 'Sala 204')")
        db.execSQL("INSERT INTO teacher_classes (time, name, students, room) VALUES ('11:00 - 12:30', 'Álgebra Lineal', '1B • 38 estudiantes', 'Sala 307')")
    }

    // --- MÉTODOS DE CONSULTA Y OPERACIONES ---

    // Validar usuario en el Login
    fun validarUsuario(email: String, password: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT rol FROM usuarios WHERE LOWER(email) = LOWER(?) AND password = ?",
            arrayOf(email.trim(), password.trim())
        )

        var rol: String? = null
        if (cursor.moveToFirst()) {
            rol = cursor.getString(0)
        }
        cursor.close()
        return rol
    }

    // Obtener tareas para el estudiante
    fun getTasks(): List<TaskModel> {
        val tasks = mutableListOf<TaskModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, title, subtitle, badge FROM tasks", null)
        if (cursor.moveToFirst()) {
            do {
                tasks.add(
                    TaskModel(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        subtitle = cursor.getString(2),
                        badge = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }

    // Obtener clases asignadas al docente
    fun getTeacherClasses(): List<ClassModel> {
        val classes = mutableListOf<ClassModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, time, name, students, room FROM teacher_classes", null)
        if (cursor.moveToFirst()) {
            do {
                classes.add(
                    ClassModel(
                        id = cursor.getInt(0),
                        time = cursor.getString(1),
                        name = cursor.getString(2),
                        students = cursor.getString(3),
                        room = cursor.getString(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return classes
    }
}