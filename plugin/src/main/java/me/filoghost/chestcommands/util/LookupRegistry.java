package me.filoghost.chestcommands.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.CaseInsensitiveHashMap;
import me.filoghost.fcommons.collection.CaseInsensitiveMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LookupRegistry<V> {
    private static final char[] KEY_IGNORE_CHARS = new char[] { '-', '_', ' ' };
    private final CaseInsensitiveMap<V> valuesMap = new CaseInsensitiveHashMap();

    public static <V> LookupRegistry<V> fromValues(final V[] values, final Function<V, String> keyExtractor) {
        return LookupRegistry.fromValues((Iterable) Arrays.asList(values), keyExtractor);
    }

    public static <V> LookupRegistry<V> fromValues(final Iterable<V> values, final Function<V, String> keyExtractor) {
        final LookupRegistry<V> registry = new LookupRegistry();
        registry.putAll(values, keyExtractor);
        return registry;
    }

    public static <V> LookupRegistry<Enchantment> fromValues(final Iterator<Enchantment> values) {
        final LookupRegistry<Enchantment> registry = new LookupRegistry<Enchantment>();
        while (values.hasNext()) {
            final Enchantment value = (Enchantment) values.next();
            registry.put(((Enchantment) value).getKey().toString(), value);
        }
        return registry;
    }

    protected LookupRegistry() {}

    public @Nullable V lookup(final String key) { return key == null ? null : this.valuesMap.get(this.removeIgnoredChars(key)); }

    protected void putAll(final Iterable<V> values, final Function<V, String> keyExtractor) {
        final Iterator var3 = values.iterator();

        while (var3.hasNext()) {
            final V value = (V) var3.next();
            this.put((String) keyExtractor.apply(value), value);
        }

    }

    public void put(final String key, final V value) { this.valuesMap.put(this.removeIgnoredChars(key), value); }

    private String removeIgnoredChars(final String valueName) { return Strings.stripChars(valueName, LookupRegistry.KEY_IGNORE_CHARS); }

    @Override
    public String toString() { return "LookupRegistry [values=" + this.valuesMap + "]"; }
}
