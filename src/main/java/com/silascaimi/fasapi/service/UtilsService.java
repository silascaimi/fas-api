package com.silascaimi.fasapi.service;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UtilsService {
	
	@SuppressWarnings({ "unchecked" })
	public <T> T merge(Map<String, Object> campos, T objDestino, Class<T> clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		T objOrigem = objectMapper.convertValue(campos, clazz);

		campos.forEach((nomePropiedade, valorPropiedade) -> {
			Field field = ReflectionUtils.findField(clazz, nomePropiedade);
			field.setAccessible(true);

			Object novoValor = (valorPropiedade instanceof Map) 
					? merge((Map<String, Object>) valorPropiedade, 
							(T) ReflectionUtils.getField(field, objDestino), 
							(Class<T>) field.getType()) 
					: ReflectionUtils.getField(field, objOrigem);

			ReflectionUtils.setField(field, objDestino, novoValor);
		});

		return objDestino;
	}

}
