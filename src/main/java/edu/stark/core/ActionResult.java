package edu.stark.core;

public record ActionResult(boolean success, String message) {
    public static ActionResult ok(String message) { return new ActionResult(true, message); }
    public static ActionResult fail(String message) { return new ActionResult(false, message); }
    public ActionResult add(String extra) {
        return extra == null || extra.isBlank() ? this : new ActionResult(success, message + "\n" + extra);
    }
}