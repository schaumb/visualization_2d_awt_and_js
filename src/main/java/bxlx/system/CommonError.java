package bxlx.system;

/**
 * Created by qqcs on 2016.12.23..
 */
public class CommonError extends def.js.Error {
    public CommonError(String name, String message) {
        super(message);
        if (this.message == null) {
            this.message = message;
        }
        if (this.name == null) {
            this.name = name;
        }
    }
}
