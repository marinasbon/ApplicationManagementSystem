import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//classe de interface de gerenciamento dos clientes
public class InterfaceClientes extends JFrame {

    private Gestor gestor;
    private JPanel painelClientes;
    private JTextArea outputArea;

    public InterfaceClientes(Gestor gestor) {
        this.gestor = gestor;

        setTitle("Gerenciar Clientes");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        painelClientes = new JPanel(new GridLayout(0, 1));

        // criacao do painel com todos os clientes
        for (Cliente cl : gestor.listarClientes()) {
            JButton botCliente = new JButton(cl.toString());
            painelClientes.add(botCliente);

            botCliente.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listarAssinaturas(cl);
                }
            });
        }

        // botao para listar clientes a cobrar
        JButton clientesButton = new JButton("Listar Clientes a Cobrar");
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarDevedores();
            }
        });
        // botao para cadastrar um cliente novo
        JButton novoClienteButton = new JButton("Cadastrar Novo Cliente");
        novoClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCliente();
            }
        });

        JScrollPane scrollPane = new JScrollPane(painelClientes);
        setLayout(new BorderLayout());
        JPanel optionsPanel = new JPanel(new GridLayout(3, 0)); // Ajuste aqui para acomodar o novo botão
        optionsPanel.add(novoClienteButton);
        optionsPanel.add(clientesButton);
        add(optionsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addCliente() {
        JTextField cpfField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] message = {
                "CPF:", cpfField,
                "Nome:", nomeField,
                "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Novo Cliente",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String cpf = cpfField.getText();
            String nome = nomeField.getText();
            String email = emailField.getText();

            // verifica se o cpf é valido
            if (!cpf.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "Insira um CPF válido.",
                        "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // verifica se o cpf já existe
            if (gestor.listarClientes().stream().anyMatch(cliente -> cliente.getCpf().equals(cpf))) {
                JOptionPane.showMessageDialog(this, "CPF já cadastrado. Insira um CPF único.",
                        "Erro de CPF", JOptionPane.ERROR_MESSAGE);
            } else {
                gestor.cadastrarOuEditarCliente(cpf, nome, email);
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso.",
                        "Cadastro de Cliente", JOptionPane.INFORMATION_MESSAGE);
                gestor.salvaClientesArquivo();

                atualizarListaClientes();
            }

        }
    }

    private void atualizarListaClientes() {
        painelClientes.removeAll();

        // adiciona os botões de clientes atualizados
        for (Cliente cl : gestor.listarClientes()) {
            JButton botCliente = new JButton(cl.toString());
            painelClientes.add(botCliente);

            botCliente.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    listarAssinaturas(cl);
                }
            });
        }

        // atualiza o layout do painel
        revalidate();
        repaint();
    }

    private void listarAssinaturas(Cliente cl) {
        List<String> assinaturasCliente = gestor.listarAssinaturasCliente(cl.getCpf());
    
        // Criar uma nova janela para exibir as assinaturas do cliente
        JFrame assinaturasFrame = new JFrame("Assinaturas de " + cl.getNome());
        assinaturasFrame.setSize(400, 300);
        assinaturasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        assinaturasFrame.setLocationRelativeTo(null);
    
        //botao para remover um cliente
        JButton removerClienteButton = new JButton("Remover Cliente");
        removerClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // remover o cliente e atualizar a lista no painel
                gestor.removerCliente(cl);
                atualizarListaClientes();
                //mensagem de confirmacao
                JOptionPane.showMessageDialog(removerClienteButton, "Cliente removido com sucesso.",
                        "Remoção de Cliente", JOptionPane.INFORMATION_MESSAGE);
                // fechar a janela de assinaturas
                assinaturasFrame.dispose();
            }
        });
    
        //criacao de um painel para exibir as assinaturas
        JPanel assinaturasPanel = new JPanel(new BorderLayout());
        JTextArea assinaturasTextArea = new JTextArea();
        assinaturasTextArea.setEditable(false);
    
        //adicionando as assinaturas na area de texto
        for (String assinatura : assinaturasCliente) {
            assinaturasTextArea.append(assinatura + "\n");
        }
    
        assinaturasPanel.add(new JScrollPane(assinaturasTextArea), BorderLayout.CENTER);
        assinaturasPanel.add(removerClienteButton, BorderLayout.SOUTH);
    
        assinaturasFrame.add(assinaturasPanel);
        assinaturasFrame.setVisible(true);
    }
    

    private void listarDevedores() {
        //abre uma janela que lista os clientes
        JFrame janelaCli = new JFrame("Clientes a Cobrar");
        janelaCli.setSize(400, 300);
        janelaCli.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaCli.setLocationRelativeTo(null);
    
        JTextArea clientesParaCobrarTextArea = new JTextArea();
        clientesParaCobrarTextArea.setEditable(false);
    
        for (String cliente : gestor.clientesParaCobrar()) {
            clientesParaCobrarTextArea.append(cliente + "\n");
        }
    
        JScrollPane scrollPane = new JScrollPane(clientesParaCobrarTextArea);
        janelaCli.add(scrollPane);
        janelaCli.setVisible(true);
    }
    
}