package edu.pse.beast.zzz.toolbox;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * I18N utility class..
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 *
 *         Taken from: https://www.sothawo.com/2016/09/
 *         how-to-implement-a-javafx-ui-where-the-language-can-be-changed-dynamically/
 */
public final class Internationalization {
    /** The current selected Locale. */
    private static final ObjectProperty<Locale> LOCALE;

    /**
     * Instantiates a new internationalization.
     */
    private Internationalization() { }

    static {
        LOCALE = new SimpleObjectProperty<Locale>(getDefaultLocale());
        LOCALE.addListener((observable, oldValue, newValue)
            -> Locale.setDefault(newValue));
    }

    /**
     * Get the supported Locales.
     *
     * @return List of Locale objects.
     */
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<Locale>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    /**
     * Get the default LOCALE. This is the systems default if contained in the
     * supported locales, english otherwise.
     *
     * @return the locale
     */
    public static Locale getDefaultLocale() {
        final Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault)
                ? sysDefault : Locale.ENGLISH;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public static Locale getLocale() {
        return LOCALE.get();
    }

    /**
     * Sets the locale.
     *
     * @param locale
     *            the new locale
     */
    public static void setLocale(final Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    /**
     * Locale property.
     *
     * @return the object property
     */
    public static ObjectProperty<Locale> localeProperty() {
        return LOCALE;
    }

    /**
     * Gets the string with the given key from the resource bundle for the
     * current LOCALE and uses it as first argument to MessageFormat.format,
     * passing in the optional arguments and returning the result.
     *
     * @param key
     *            message key
     * @param args
     *            optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        final ResourceBundle bundle =
                ResourceBundle.getBundle("messages", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * Creates a String binding to a localized String for the given message
     * bundle key.
     *
     * @param key
     *            key
     * @param args
     *            arguments
     * @return String binding
     */
    public static StringBinding createStringBinding(final String key,
                                                    final Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), LOCALE);
    }

    /**
     * Creates a String binding to a localized String that is computed by
     * calling the given function.
     *
     * @param func
     *            function called on every change
     * @return StringBinding
     */
    public static StringBinding createStringBinding(final Callable<String> func) {
        return Bindings.createStringBinding(func, LOCALE);
    }

    /**
     * Creates a bound Label whose value is computed on language change.
     *
     * @param func
     *            the function to compute the value
     * @return Label
     */
    public static Label labelForValue(final Callable<String> func) {
        final Label label = new Label();
        label.textProperty().bind(createStringBinding(func));
        return label;
    }

    /**
     * Creates a bound Button for the given resource bundle key.
     *
     * @param key
     *            ResourceBundle key
     * @param args
     *            optional arguments for the message
     * @return Button
     */
    public static Button buttonForKey(final String key, final Object... args) {
        final Button button = new Button();
        button.textProperty().bind(createStringBinding(key, args));
        return button;
    }

    /**
     * Creates a bound tool tip for the given resource bundle key.
     *
     * @param key
     *            ResourceBundle key
     * @param args
     *            optional arguments for the message
     * @return Label
     */
    public static Tooltip tooltipForKey(final String key,
                                        final Object... args) {
        final Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(createStringBinding(key, args));
        return tooltip;
    }
}