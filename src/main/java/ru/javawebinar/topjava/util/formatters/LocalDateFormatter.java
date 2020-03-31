package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object.toString();
    }


    public static class LocalDateAnnotationFormatterFactory implements AnnotationFormatterFactory<LocalDateFormat> {
        @Override
        public Set<Class<?>> getFieldTypes() {
            return Collections.singleton(LocalDate.class);
        }

        @Override
        public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
            return new LocalDateFormatter();
        }

        @Override
        public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType) {
            return new LocalDateFormatter();
        }
    }
}
