package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return StringUtils.isEmpty(text) ? null : LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.toString();
    }

    public static class LocalTimeAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalTimeFormat> {
        @Override
        public Set<Class<?>> getFieldTypes() {
            return Collections.singleton(LocalTime.class);
        }

        @Override
        public Printer<?> getPrinter(LocalTimeFormat annotation, Class<?> fieldType) {
            return new LocalTimeFormatter();
        }

        @Override
        public Parser<?> getParser(LocalTimeFormat annotation, Class<?> fieldType) {
            return new LocalTimeFormatter();
        }
    }
}
