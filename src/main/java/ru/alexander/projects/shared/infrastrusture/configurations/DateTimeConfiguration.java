package ru.alexander.projects.shared.infrastrusture.configurations;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfiguration {
    private static final String DATE_FORMAT = "dd-MM-YYYY";

    private static final String DATE_TIME_FORMAT = "dd-MM-YYYY HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            final var dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            final var dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

            builder
                    .serializers(new LocalDateSerializer(dateFormatter))
                    .deserializers(new LocalDateDeserializer(dateFormatter))
                    .serializers(new LocalDateTimeSerializer(dateTimeFormatter))
                    .deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}
