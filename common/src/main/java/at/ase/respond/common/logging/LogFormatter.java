package at.ase.respond.common.logging;

public final class LogFormatter {

    /**
     * Formats a string template with arguments.
     *
     * @param template The template string containing "{}" placeholders.
     * @param args     The arguments to replace in the template.
     * @return A formatted string with replaced placeholders.
     */
    public static String format(String template, Object... args) {
        StringBuilder sb = new StringBuilder(template);
        int argsIndex = 0;

        // Iterate through the template and replace "{}" with corresponding arguments
        for (int i = 0; i < sb.length() - 1; i++) {
            if (sb.charAt(i) == '{' && sb.charAt(i + 1) == '}') {
                if (argsIndex < args.length) {
                    // Replace "{}" with the argument at args[argsIndex]
                    sb.replace(i, i + 2, String.valueOf(args[argsIndex]));
                    i += String.valueOf(args[argsIndex]).length() - 1;
                    argsIndex++;
                } else {
                    // If there are more "{}" than arguments, break out
                    break;
                }
            }
        }

        return sb.toString();
    }
}
