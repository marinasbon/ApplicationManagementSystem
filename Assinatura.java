public class Assinatura {
    private int codigo;
    private int codigoApp;
    private String cpfCliente;
    private String dataInicio;
    private String dataEncerramento;
    private boolean pago;

    public Assinatura(int codigo, int codigoApp, String cpfCliente, String dataInicio, String dataEncerramento,
            boolean pago) {
        this.codigo = codigo;
        this.codigoApp = codigoApp;
        this.cpfCliente = cpfCliente;
        this.dataInicio = dataInicio;
        this.dataEncerramento = dataEncerramento;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getCodigoApp() {
        return codigoApp;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(String dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean b) {
        pago = b;
    }

    @Override
    public String toString() {
        return "Assinatura [codigo =" + codigo + ", codigoApp =" + codigoApp + ", cpfCliente =" + cpfCliente
                + ", dataInicio = "
                + dataInicio + ", dataEncerramento = " + dataEncerramento + ", pago = " + pago + "]";
    }
}
