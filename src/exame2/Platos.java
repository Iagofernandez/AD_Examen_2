
package exame2;

import java.io.Serializable;

public class Platos implements Serializable{
    private String codp;
   private String nome;
   
   private int grasaTotal;
public Platos(){
    
}
	

        public Platos(String codp, String nome, int grasaTotal )
	{
		this.codp = codp;
		this.nome = nome;
                this.grasaTotal = grasaTotal;
	}

    public String getCodp() {
        return codp;
    }

    public void setCodp(String codp) {
        this.codp = codp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getGrasaTotal() {
        return grasaTotal;
    }

    public void setGraxaTotal(int graxaTotal) {
        this.grasaTotal = grasaTotal;
    }

    @Override
    public String toString() {
        return "Platos{" + "codp=" + codp + ", nomep=" + nome + ", grasaTotal=" + grasaTotal + '}';
    }
        

	
}
