package vlfsoft.common.spring.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import vlfsoft.patterns.StructuralPattern;
import vlfsoft.common.util.ClassUtils;

final public class Springi18nUtils {

    public static class MessageNotFoundException extends RuntimeException {
        MessageNotFoundException(String s) {
            super(s);
        }
    }

    private Springi18nUtils() {
    }

    @StructuralPattern.ShorthandPattern
    public static Optional<String> getMessage(final @NotNull MessageSource aMessageSource, @NotNull String aKey) {
        try {
            return Optional.of(aMessageSource.getMessage(aKey, null, LocaleContextHolder.getLocale()));
        } catch (NoSuchMessageException e) {
            return Optional.empty();
        }
    }

    @StructuralPattern.ShorthandPattern
    public static String getMessageOrElseThrow(final @NotNull MessageSource aMessageSource, @NotNull String aKey)
            throws MessageNotFoundException {
        return getMessage(aMessageSource, aKey)
                //.orElseThrow(IllegalStateException::new);
                .orElseThrow(() -> new MessageNotFoundException(String.format(Locale.getDefault(), "aKey = '%s'", aKey)));
    }

    @StructuralPattern.ShorthandPattern
    public static String getMessageOrElse(final @NotNull MessageSource aMessageSource, @NotNull String aKey, @NotNull String aDefaultValue) {
        return getMessage(aMessageSource, aKey)
                //.orElseThrow(IllegalStateException::new);
                .orElse(aDefaultValue);
    }

    public static <E extends Enum> String getEnumStringValue(final @NotNull MessageSource aMessageSource,
                                                             final @NotNull E aEnumType)
            throws MessageNotFoundException {
        return getMessageOrElseThrow(aMessageSource, ClassUtils.getNestedClassName(aEnumType.getClass()) + "." + aEnumType.name());
    }

    private static <E extends Enum> Optional<E> getEnumOptional(@NotNull String aTitle, final @NotNull MessageSource aMessageSource,
                                                               @NotNull String aNameSpace, final @NotNull E aEnumType) {

        // Optional.empty() or ТВ, ...
        Optional<String> enumStringValueOptional = getMessage(aMessageSource, aNameSpace + "." + aEnumType.name());

        return (enumStringValueOptional.isPresent() && aTitle.equals(enumStringValueOptional.get())
                ? Optional.of(aEnumType)
                : Optional.empty()
        );
    }

    private static <E extends Enum> Optional<E> getEnumOptional(@NotNull String aTitle, final @NotNull MessageSource aMessageSource,
                                                               final @NotNull E aEnumType) {
        return getEnumOptional(aTitle, aMessageSource,
                ClassUtils.getNestedClassName(aEnumType.getClass()),
                aEnumType);
    }

    public static <E extends Enum<E>> E getInstanceOf(Class<E> aEnumClass, final @NotNull MessageSource aMessageSource, @NotNull String aTitle)
            throws MessageNotFoundException {
        return EnumSet.allOf(aEnumClass).stream()
                .filter((enumValue) -> Springi18nUtils.getEnumOptional(aTitle, aMessageSource, enumValue).isPresent())
                .findFirst()
                .orElseThrow(() -> new MessageNotFoundException(String.format(Locale.getDefault(), "aTitle = '%s'", aTitle)));
    }

    public static <E extends Enum<E>> E getInstanceOf(Class<E> aEnumClass, final @NotNull MessageSource aMessageSource, @NotNull String aTitle,
                                                   final @NotNull E aDefaultValue) {
        return EnumSet.allOf(aEnumClass).stream()
                .filter((enumValue) -> Springi18nUtils.getEnumOptional(aTitle, aMessageSource, enumValue).isPresent())
                .findFirst()
                .orElse(aDefaultValue);
    }

    @NotNull
    public static String getNoDataAvailableTitle(final @NotNull MessageSource aMessageSource) {
        return Springi18nUtils.getMessageOrElse(aMessageSource, "NoDataAvailable", "No data available");
    }

}
