package org.catalogo.repository;

public interface ConversorDatosI {
    <T> T getData(String json, Class<T> tClass);
}