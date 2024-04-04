public class App {
    public static void main(String[] args) {
        InterfacePri interfac = new InterfacePri(new Gestor());
        interfac.atualizarListaAplicativos(); // carregar os aplicativos iniciais
        interfac.setVisible(true);
    }
}
