public class Aplicativo {
    public enum sistemaOp {Android, IOS, Windows, MacOS}
    private sistemaOp sis;
    int codigo;
    String nome;
    double valorMensal;

    public Aplicativo(int codigo, String nome, Aplicativo.sistemaOp sistemaOp, double valorMensal){
        this.codigo = codigo;
        this.nome = nome;
        this.sis = sistemaOp;
        this.valorMensal = valorMensal;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public sistemaOp getSistemaOp() {
        return sis;
    }

    public double getValorMensal() {
        return valorMensal;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSistemaOp(Aplicativo.sistemaOp sistemaOp) {
        this.sis = sistemaOp;
    }

    public void setValorMensal(double valorMensal) {
        this.valorMensal = valorMensal;
    }

    @Override
    public String toString() {
        return "Aplicativo [codigo=" + codigo + ", nome=" + nome + ", sistemaOp=" + sis.toString() + ", valorMensal="
                + valorMensal + "]";
    }
    
}