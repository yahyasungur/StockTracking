package com.example.stocktracking.data.csv

import java.io.InputStream

// Logic is taken from Phillip Lackner --> Github
interface CSVParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}