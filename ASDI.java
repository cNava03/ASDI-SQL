import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ASDI implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private boolean sinc = false;
    private Token preanalisis;
    private final List<Token> tokens;
    Deque<Object> pila = new ArrayDeque<>();
    int longitud;

    //No Terminales de la gramatica
    List<String> TAS = new ArrayList<>();

    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        longitud = this.tokens.size();

        pila.push(TipoToken.EOF);
        pila.push("Q");

        TAS.add("Q");
        TAS.add("D");
        TAS.add("P");
        TAS.add("A");
        TAS.add("A1");
        TAS.add("A2");
        TAS.add("A3");
        TAS.add("T");
        TAS.add("T1");
        TAS.add("T2");
        TAS.add("T3");
    }

    @Override
    public boolean parse() {
        while(true){
            if(TAS.contains(pila.peek())){
                //Fila Q
                if (pila.peek() == "Q") {
                    //Q -> select D from T
                    if(preanalisis.tipo == TipoToken.SELECT){
                        pila.pop();
                        pila.push("T");
                        pila.push(TipoToken.FROM);
                        pila.push("D");
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    // Q -> sinc
                    else if(preanalisis.tipo == TipoToken.EOF){
                        pila.pop();
                        sinc = true;
                    }
                    else{
                        hayErrores = true;
                        System.out.println("Error: se esperaba \"select\"");
                        break;
                    }
                }
                //Fila D
                else if (pila.peek() == "D"){
                    // D -> distinct P
                    if(preanalisis.tipo == TipoToken.DISTINCT){
                        pila.pop();
                        pila.push("P");
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    // D -> P
                    else if(preanalisis.tipo == TipoToken.IDENTIFICADOR || preanalisis.tipo == TipoToken.ASTERISCO){
                        pila.pop();
                        pila.push("P");
                    }
                    else{
                        hayErrores = true;
                        System.out.println("Error: Se esperaba \"*\" o un IDENTIFICADOR.");
                        break;
                    }
                }
                //Fila P
                else if (pila.peek() == "P"){
                    // P -> A
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("A");
                    }
                    // P -> *
                    else if(preanalisis.tipo == TipoToken.ASTERISCO){
                        pila.pop();
                        i++;
                        preanalisis = tokens.get(i);
                    }
                    else{
                        hayErrores = true;
                        System.out.println("Error: Se esperaba \"*\" o un IDENTIFICADOR.");
                        break;
                    }
                }
                //Fila A
                else if (pila.peek() == "A"){
                    // A -> A2 A1
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.pop();
                        pila.push("A1");
                        pila.push("A2");
                    }
                    else{
                        hayErrores = true;
                        System.out.println("Error: Se esperaba un IDENTIFIER.");
                        break;
                    }
                }
            }
        }
        if(sinc){
            return false;
        }
        else{
            if(hayErrores){
                return false;
            }
            else{
                System.out.println("The query is correct.");
                return true;
            }
        }
    }
}
