package nl.openminetopia.utils.string;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public class ReplaceableString {

    private String str;

    /* This is more performant than String#replace, apparently... */
    public ReplaceableString replace(String search, String replacement) {
        str = StringUtils.replace(str, search, replacement);

        return this;
    }

    public String get() {
        return str;
    }

    public static ReplaceableString of(final String str) {
        return new ReplaceableString(str);
    }

}
