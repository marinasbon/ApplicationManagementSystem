import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfacePri extends JFrame {

    private Gestor gestor;
    private JPanel painelApps;

    // janela inicial
    public InterfacePri(Gestor gestor) {
        this.gestor = gestor;

        // criando a caixa inicial
        setTitle("Gestor de Aplicativos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        painelApps = new JPanel(new GridLayout(0, 1));

        // botao para ir para o gerenciamento de clientes
        JButton clientesButton = new JButton("Gerenciar Clientes");
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceClientes iClientes = new InterfaceClientes(gestor);
                iClientes.setVisible(true);
            }
        });
        // botao para cadastrar um aplicativo
        JButton novoAppButton = new JButton("Adicionar Novo Aplicativo");
        novoAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOuEditaApp(null, "", null, null);
            }
        });

        JScrollPane scrollPane = new JScrollPane(painelApps);

        setLayout(new BorderLayout());
        JPanel optionsPanel = new JPanel(new GridLayout(2, 0));
        optionsPanel.add(novoAppButton);
        optionsPanel.add(clientesButton);
        add(optionsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // janela de criacao/edicao de aplicativos
    private void addOuEditaApp(Integer codIni, String nomeIni, Aplicativo.sistemaOp sis, Double valor) {
        // mostra nas caixas de input as informacoes do app, se nao existir o app
        // aparece em branco
        String codStr = codIni == null ? "" : String.valueOf(codIni);
        JTextField codigoField = new JTextField(codStr);
        JTextField nomeField = new JTextField(nomeIni);
        String valorStr = valor == null ? "" : String.valueOf(valor);
        JTextField valorMensalField = new JTextField(valorStr);

        // se for fazer uma edição, nao é possivel alterar o nome ou o codigo
        if (codIni != null) {
            codigoField.setEditable(false);
            nomeField.setEditable(false);
        }
        // caixa de opcoes para selecionar o sistema operacional
        Aplicativo.sistemaOp[] sistemasOp = Aplicativo.sistemaOp.values();
        JComboBox<Aplicativo.sistemaOp> sistemaOpComboBox = new JComboBox<>(sistemasOp);
        sistemaOpComboBox.setSelectedItem(sis);

        Object[] message = {
                "Código:", codigoField,
                "Nome:", nomeField,
                "Sistema Operacional:", sistemaOpComboBox,
                "Valor Mensal:", valorMensalField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Adicionar Novo Aplicativo",
                JOptionPane.OK_CANCEL_OPTION);

        // parte de insercao dos dados
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nomeApp = nomeField.getText();
                int codigo = Integer.parseInt(codigoField.getText());
                double valorMensal = Double.parseDouble(valorMensalField.getText());
                Aplicativo.sistemaOp sistemaOp = (Aplicativo.sistemaOp) sistemaOpComboBox.getSelectedItem();
                // adiciona o aplicativo na lista e faz o display dele no menu
                gestor.cadastrarOuEditarAplicativo(codigo, nomeApp, sistemaOp, valorMensal);
                // se for a criação de um app novo, adiciona no painel
                if (codIni == null) {
                    addListaApps(codigo, nomeApp);
                }
                gestor.salvaAplicativosArquivo();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Insira valores válidos para o código e/ou o valor mensal.",
                        "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addListaApps(int codigo, String nomeApp) {
        JButton appButton = new JButton(nomeApp);
        appButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOpcoesApp(gestor.getAplicativo(codigo));
            }
        });
        painelApps.add(appButton);
        revalidate();
        repaint();
    }

    public void atualizarListaAplicativos() {
        gestor.getAplicativos().stream().forEach(app -> {
            addListaApps(app.getCodigo(), app.getNome());
        });
    }

    private void showOpcoesApp(Aplicativo app) {
        String[] options = { "Gerenciar Assinaturas", "Editar Informações do Aplicativo", "Listar Faturamento Mensal" };
        int choice = JOptionPane.showOptionDialog(this,
                "Escolha uma opção para o aplicativo '" + app.getNome() + "':",
                "Opções do Aplicativo", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);

        if (choice == 0) {
            // gerenciar assinaturas
            InterfaceAssi secAssinaturas = new InterfaceAssi(gestor, app);
            secAssinaturas.setVisible(true);
        } else if (choice == 1) {
            addOuEditaApp(app.getCodigo(), app.getNome(), app.getSistemaOp(), (double) app.getValorMensal());
        } else if(choice == 2){
            JOptionPane.showMessageDialog(this, gestor.listarFaturamentoPorAplicativo(app),
                        "Faturamento Mensal", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}