import com.sun.istack.internal.NotNull;

public class BinaryUtils {

    @NotNull
    public String getZeroPadding(int paddingLength) {
        StringBuilder zeroBuilder = new StringBuilder(paddingLength);
        for (int i = 0; i < paddingLength; i++) {
            zeroBuilder.append("0");
        }
        return zeroBuilder.toString();
    }
}
