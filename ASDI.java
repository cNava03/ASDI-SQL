import java.util.List;

public class ASDI implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        //preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        return false;
    }
}
