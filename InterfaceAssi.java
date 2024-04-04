import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//classe de interface de gerenciamento das assinaturas de um aplicativo
public class InterfaceAssi extends JFrame {

    private Gestor gestor;
    private Aplicativo aplicativo;
    private JPanel assinaturasPanel;

    public InterfaceAssi(Gestor gestor, Aplicativo aplicativo) {
        this.gestor = gestor;
        this.aplicativo = aplicativo;

        setTitle("Gerenciar Assinaturas: " + aplicativo.getNome());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton novaAssinaturaButton = new JButton("Adicionar Nova Assinatura");
        novaAssinaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novaAssinatura();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(novaAssinaturaButton);

        add(buttonPanel, BorderLayout.NORTH);
        assinaturasPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(assinaturasPanel);
        add(scrollPane, BorderLayout.CENTER);
        atualizarAssinaturasPanel();
    }

    private void atualizarAssinaturasPanel() {
        assinaturasPanel.removeAll(); // Remover todos os componentes existentes
        for (Assinatura assinatura : gestor.listarAssinaturasPorApp(aplicativo)) {
            addAssiPainel(assinatura);
        }
        assinaturasPanel.revalidate(); // Revalidar o painel
        assinaturasPanel.repaint(); // Repintar o painel
    }

    private void addAssiPainel(Assinatura a) {
        JButton assinaturaButton = new JButton("Assinatura " + a.getCodigo());
        assinaturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOpcoesAssinatura(a);
            }
        });
        assinaturasPanel.add(assinaturaButton);
    }

    private void novaAssinatura() {
        List<Cliente> listaClientes = gestor.listarClientes();
        String[] nomesClientes = listaClientes.stream().map(cl -> cl.getNome()).toArray(tam -> new String[tam]);
        JComboBox<String> clienteComboBox = new JComboBox<>(nomesClientes);

        JTextField codigoAssinaturaField = new JTextField();
        JTextField dataInicioField = new JTextField();
        JTextField dataEncerramentoField = new JTextField();

        Object[] message = {
                "Selecione o Cliente:", clienteComboBox,
                "Código da Assinatura:", codigoAssinaturaField,
                "Data de Início (DD/MM):", dataInicioField,
                "Data de Encerramento (DD/MM):", dataEncerramentoField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Nova Assinatura",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int indiceClienteSelecionado = clienteComboBox.getSelectedIndex();

                Cliente clienteSelecionado = listaClientes.get(indiceClienteSelecionado);

                int codigoAssinatura = Integer.parseInt(codigoAssinaturaField.getText());
                String dataInicio = dataInicioField.getText();
                String dataEncerramento = dataEncerramentoField.getText();

                gestor.registrarAssinatura(codigoAssinatura, aplicativo.getCodigo(), clienteSelecionado.getCpf(),
                        dataInicio, dataEncerramento);
                addAssiPainel(gestor.getAssinatura(codigoAssinatura));
                atualizarAssinaturasPanel();
                JOptionPane.showMessageDialog(this, "Assinatura registrada com sucesso.",
                        "Nova Assinatura", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao criar a nova assinatura.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showOpcoesAssinatura(Assinatura assinatura) {
        String[] options = { "Cancelar Assinatura", "Marcar Como Paga" };
        int choice = JOptionPane.showOptionDialog(this,
                "Escolha uma opção para a " + assinatura.toString() + ":",
                "Opções da Assinatura", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);

        if (choice == 0) {
            if (!assinatura.getDataEncerramento().equals("00/00")) {
                JOptionPane.showMessageDialog(this, "Assinatura já cancelada", "Cancelar Assinatur",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                cancelarAssinatura(assinatura);
            }
        } else if (choice == 1) {
            String message = assinatura.isPago() ? "Assinatura já paga." : "Assinatura paga com sucesso.";
            assinatura.setPago(true);
            JOptionPane.showMessageDialog(this, message, "Pagamento de Assinatura", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelarAssinatura(Assinatura assinatura) {
        String dataCancelamento = JOptionPane.showInputDialog(this,
                "Digite a data de cancelamento (DD/MM):", "Cancelar Assinatura", JOptionPane.PLAIN_MESSAGE);

        if (dataCancelamento != null) {
            gestor.cancelarAssinatura(assinatura.getCodigo(), dataCancelamento);
            JOptionPane.showMessageDialog(this, "Assinatura cancelada com sucesso.",
                    "Cancelamento de Assinatura", JOptionPane.INFORMATION_MESSAGE);

            revalidate();
            repaint();
        }
    }

}
