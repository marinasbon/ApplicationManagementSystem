import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Gestor {
    private List<Aplicativo> aplicativos;
    private List<Cliente> clientes;
    private List<Assinatura> assinaturas;

    private static final String CLIENTES_FILE = "clientes.txt";
    private static final String APLICATIVOS_FILE = "aplicativos.txt";
    private static final String ASSINATURAS_FILE = "assinaturas.txt";

    public Gestor() {
        this.aplicativos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.assinaturas = new ArrayList<>();

        loadClientesArquivo();
        loadAplicativosArquivo();
        loadAssinaturasArquivo();
    }

    private void loadClientesArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                String cpf = partes[0];
                String nome = partes[1];
                String email = partes[2];
                clientes.add(new Cliente(cpf, nome, email));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void salvaClientesArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENTES_FILE))) {
            for (Cliente cliente : clientes) {
                writer.write(cliente.getCpf() + "," + cliente.getNome() + "," + cliente.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAplicativosArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(APLICATIVOS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int codigo = Integer.parseInt(parts[0]);
                String nome = parts[1];
                Aplicativo.sistemaOp sis = Aplicativo.sistemaOp.valueOf(parts[2]);
                double valorMensal = Double.parseDouble(parts[3]);
                aplicativos.add(new Aplicativo(codigo, nome, sis, valorMensal));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void salvaAplicativosArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APLICATIVOS_FILE))) {
            for (Aplicativo app : aplicativos) {
                writer.write(app.getCodigo() + "," + app.getNome() + "," +
                        app.getSistemaOp() + "," + app.getValorMensal());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAssinaturasArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ASSINATURAS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int codigo = Integer.parseInt(parts[0]);
                int codigoApp = Integer.parseInt(parts[1]);
                String cpfCliente = parts[2];
                String dataInicio = parts[3];
                String dataEncerramento = parts[4];
                boolean pago = Boolean.parseBoolean(parts[5]); // novo atributo pago
                assinaturas.add(new Assinatura(codigo, codigoApp, cpfCliente, dataInicio, dataEncerramento, pago));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void salvaAssinaturasArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ASSINATURAS_FILE))) {
            for (Assinatura assinatura : assinaturas) {
                writer.write(assinatura.getCodigo() + "," + assinatura.getCodigoApp() + "," +
                        assinatura.getCpfCliente() + "," + assinatura.getDataInicio() + "," +
                        assinatura.getDataEncerramento() + "," + assinatura.isPago());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cadastrarOuEditarCliente(String cpf, String nome, String email) {
        Optional<Cliente> clienteExistente = clientes.stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst();

        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNome(nome);
            cliente.setEmail(email);
            System.out.println("Cliente editado com sucesso.");
        } else {
            Cliente novoCliente = new Cliente(cpf, nome, email);
            clientes.add(novoCliente);
            System.out.println("Cliente cadastrado com sucesso.");
        }
        salvaClientesArquivo();
    }

    public void removerCliente(Cliente cl) {
        clientes.remove(cl);
        salvaClientesArquivo();

        // remover as assinaturas com esse cpf
        assinaturas.stream()
                .filter(a -> a.getCpfCliente()
                        .equals(cl.getCpf()))
                .forEach(a -> removerAssinatura(a));
    }

    public void removerAssinatura(Assinatura a) {
        assinaturas.remove(a);
        salvaAssinaturasArquivo();
    }

    public void cadastrarOuEditarAplicativo(int codigo, String nome, Aplicativo.sistemaOp sistemaOp,
            double valorMensal) {
        Optional<Aplicativo> aplicativoExistente = aplicativos.stream()
                .filter(aplicativo -> aplicativo.getCodigo() == codigo)
                .findFirst();

        if (aplicativoExistente.isPresent()) {
            Aplicativo aplicativo = aplicativoExistente.get();
            aplicativo.setNome(nome);
            aplicativo.setSistemaOp(sistemaOp);
            aplicativo.setValorMensal(valorMensal);
        } else {
            Aplicativo novoAplicativo = new Aplicativo(codigo, nome, sistemaOp, valorMensal);
            aplicativos.add(novoAplicativo);
        }
        salvaAplicativosArquivo();
    }

    public void registrarAssinatura(int codigoAssinatura, int codigoAplicativo, String cpfCliente,
            String dataInicio, String dataEncerramento) {
        Assinatura novaAssinatura = new Assinatura(codigoAssinatura, codigoAplicativo, cpfCliente, dataInicio,
                dataEncerramento, false);
        assinaturas.add(novaAssinatura);
        salvaAssinaturasArquivo();
    }

    public void cancelarAssinatura(int codigoAssinatura, String dataCancelamento) {
        Optional<Assinatura> assinaturaExistente = assinaturas.stream()
                .filter(assinatura -> assinatura.getCodigo() == codigoAssinatura)
                .findFirst();

        if (assinaturaExistente.isPresent()) {
            Assinatura assinatura = assinaturaExistente.get();
            assinatura.setDataEncerramento(dataCancelamento);
        }
        salvaAssinaturasArquivo();
    }

    public List<Cliente> listarClientes() {
        return clientes.stream()
                .collect(Collectors.toList());
    }

    public List<String> listarAplicativos() {
        return aplicativos.stream()
                .map(aplicativo -> "Código: " + aplicativo.getCodigo() + " | Nome: " + aplicativo.getNome() +
                        " | SO: " + aplicativo.getSistemaOp() + " | Valor Mensal: " + aplicativo.getValorMensal())
                .collect(Collectors.toList());
    }

    public List<Aplicativo> getAplicativos() {
        return aplicativos;
    }

    public Aplicativo getAplicativo(int codigo) {
        Optional<Aplicativo> app = aplicativos.stream()
                .filter(aplicativo -> aplicativo.getCodigo() == codigo)
                .findFirst();
        return app.isPresent() ? app.get() : null;
    }

    public Assinatura getAssinatura(int codigo) {
        Optional<Assinatura> a = assinaturas.stream()
                .filter(ass -> ass.getCodigo() == codigo)
                .findFirst();
        return a.isPresent() ? a.get() : null;
    }

    public List<Assinatura> listarAssinaturasPorApp(Aplicativo app) {
        return assinaturas.stream()
                .filter(a -> a.getCodigoApp() == app.getCodigo())
                .toList();
    }

    public List<String> listarAssinaturasCliente(String cpfCliente) {
        return assinaturas.stream()
                .filter(assinatura -> assinatura.getCpfCliente().equals(cpfCliente))
                .map(assinatura -> "Código: " + assinatura.getCodigo() + " | Aplicativo: " +

                        getAplicativo(assinatura.getCodigoApp()).getNome() + " | Início: " + assinatura.getDataInicio()
                        +
                        " | Encerramento: " + assinatura.getDataEncerramento())
                .collect(Collectors.toList());
    }

    public List<String> listarAssinantesProduto(int codigoApp) {
        return assinaturas.stream()
                .filter(assinatura -> assinatura.getCodigoApp() == codigoApp)
                .map(assinatura -> {
                    Cliente cliente = clientes.stream()
                            .filter(c -> c.getCpf().equals(assinatura.getCpfCliente()))
                            .findFirst()
                            .orElse(null);

                    return "CPF: " + cliente.getCpf() + " | Nome: " + cliente.getNome() +
                            " | Email: " + cliente.getEmail();
                })
                .collect(Collectors.toList());
    }

    public List<String> clientesParaCobrar() {
        List<String> clientesCobrar = new LinkedList<>();

        for (Cliente cliente : clientes) {
            double valorDevido = calcularValorDevido(cliente.getCpf());
            if (valorDevido > 0) {
                clientesCobrar.add(cliente.toString() + " | Valor Devido: R$" + valorDevido);
            }
        }
        return clientesCobrar;
    }

    private double calcularValorDevido(String cpfCliente) {
        return assinaturas.stream()
                .filter(a -> a.getCpfCliente().equals(cpfCliente) && !a.isPago())
                .mapToDouble(a -> {
                    Aplicativo app = getAplicativo(a.getCodigoApp());
                    return app != null ? app.getValorMensal() : 0.0;
                })
                .sum();
    }

    public String listarFaturamentoPorAplicativo(Aplicativo app) {
        StringBuilder str = new StringBuilder();
        str.append("Faturamento do mês para: ");
        str.append(app.getNome());
        str.append("\nR$");
        double faturamento = (assinaturas.stream()
                .filter(a -> a.getCodigoApp() == app.getCodigo())
                .count()) * app.getValorMensal();
        str.append(faturamento);
        return str.toString();
    }
}
