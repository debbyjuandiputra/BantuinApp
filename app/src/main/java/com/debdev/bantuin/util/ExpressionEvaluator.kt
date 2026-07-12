package com.debdev.bantuin.util

import kotlin.math.*

/**
 * Evaluator ekspresi matematika sederhana (recursive-descent parser).
 * Mendukung: + - * / ^ % (mod), tanda kurung, fungsi sin/cos/tan/asin/acos/atan/
 * sqrt/log/ln/abs, konstanta pi & e, dan faktorial (!) postfix.
 * Sudut trigonometri dalam derajat.
 */
object ExpressionEvaluator {

    class EvalException(message: String) : Exception(message)

    fun evaluate(rawExpression: String): Double {
        val expr = rawExpression
            .replace(" ", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", "pi")
        if (expr.isBlank()) throw EvalException("Ekspresi kosong")
        val parser = Parser(expr)
        val result = parser.parseExpression()
        if (!parser.isAtEnd()) throw EvalException("Ekspresi tidak valid")
        return result
    }

    private class Parser(private val text: String) {
        private var pos = 0

        fun isAtEnd() = pos >= text.length

        fun parseExpression(): Double = parseAddSub()

        private fun parseAddSub(): Double {
            var value = parseMulDiv()
            while (!isAtEnd()) {
                when (peek()) {
                    '+' -> { pos++; value += parseMulDiv() }
                    '-' -> { pos++; value -= parseMulDiv() }
                    else -> return value
                }
            }
            return value
        }

        private fun parseMulDiv(): Double {
            var value = parsePow()
            while (!isAtEnd()) {
                when (peek()) {
                    '*' -> { pos++; value *= parsePow() }
                    '/' -> {
                        pos++
                        val divisor = parsePow()
                        if (divisor == 0.0) throw EvalException("Tidak bisa dibagi nol")
                        value /= divisor
                    }
                    '%' -> { pos++; value %= parsePow() }
                    else -> return value
                }
            }
            return value
        }

        private fun parsePow(): Double {
            val base = parseUnary()
            if (!isAtEnd() && peek() == '^') {
                pos++
                val exponent = parsePow() // kanan-asosiatif
                return base.pow(exponent)
            }
            return base
        }

        private fun parseUnary(): Double {
            if (!isAtEnd() && peek() == '-') {
                pos++
                return -parseUnary()
            }
            if (!isAtEnd() && peek() == '+') {
                pos++
                return parseUnary()
            }
            return parsePostfix()
        }

        private fun parsePostfix(): Double {
            var value = parseAtom()
            while (!isAtEnd() && peek() == '!') {
                pos++
                value = factorial(value)
            }
            return value
        }

        private fun parseAtom(): Double {
            if (isAtEnd()) throw EvalException("Ekspresi tidak lengkap")

            if (peek() == '(') {
                pos++
                val value = parseExpression()
                expect(')')
                return value
            }

            if (peek().isDigit() || peek() == '.') {
                return parseNumber()
            }

            if (peek().isLetter()) {
                val name = parseIdentifier()
                return when (name) {
                    "pi" -> PI
                    "e" -> E
                    "sin" -> sin(Math.toRadians(parseArg()))
                    "cos" -> cos(Math.toRadians(parseArg()))
                    "tan" -> tan(Math.toRadians(parseArg()))
                    "asin" -> Math.toDegrees(asin(parseArg()))
                    "acos" -> Math.toDegrees(acos(parseArg()))
                    "atan" -> Math.toDegrees(atan(parseArg()))
                    "sqrt" -> sqrt(parseArg())
                    "log" -> log10(parseArg())
                    "ln" -> ln(parseArg())
                    "abs" -> abs(parseArg())
                    else -> throw EvalException("Fungsi tidak dikenal: $name")
                }
            }

            throw EvalException("Karakter tidak dikenal: ${peek()}")
        }

        private fun parseArg(): Double {
            expect('(')
            val value = parseExpression()
            expect(')')
            return value
        }

        private fun parseNumber(): Double {
            val start = pos
            while (!isAtEnd() && (peek().isDigit() || peek() == '.')) pos++
            return text.substring(start, pos).toDoubleOrNull() ?: throw EvalException("Angka tidak valid")
        }

        private fun parseIdentifier(): String {
            val start = pos
            while (!isAtEnd() && peek().isLetter()) pos++
            return text.substring(start, pos)
        }

        private fun peek(): Char = text[pos]

        private fun expect(c: Char) {
            if (isAtEnd() || peek() != c) throw EvalException("Diharapkan '$c'")
            pos++
        }

        private fun factorial(n: Double): Double {
            if (n < 0 || n != floor(n)) throw EvalException("Faktorial hanya untuk bilangan bulat non-negatif")
            var result = 1.0
            var i = 2
            while (i <= n.toInt()) {
                result *= i
                i++
            }
            return result
        }
    }
}
